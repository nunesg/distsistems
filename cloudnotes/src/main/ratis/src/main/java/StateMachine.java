import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class StateMachine extends BaseStateMachine
{
    private Map<String, String> key2values = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<Message> query(Message request) {
        final String[] opKey = request.getContent().toString(Charset.defaultCharset()).split("\\|");
        // final String result = opKey[0]+ ":"+ key2values.get(opKey[1]);
        final String result;
        switch (opKey[0]) {
            case "get":
                result = get(opKey[1]);
                LOG.info("{}: {} = {}", opKey[0], opKey[1], result);
                break;
            case "getall":
                result = getAll();
                LOG.info("{} = {}", opKey[0], result);
                break;
            default:
                result = null;
                LOG.info("{} = {}", opKey[0], result);
        }
        return CompletableFuture.completedFuture(Message.valueOf(result));
    }


    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
        final RaftProtos.LogEntryProto entry = trx.getLogEntry();
        final RaftProtos.RaftPeerRole role = trx.getServerRole();
        final String[] opKeyValue = entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset()).split("\\|");
        // final String result = opKeyValue[0]+ ":"+ key2values.put(opKeyValue[1], opKeyValue[2]);
        final String result;

        switch (opKeyValue[0]) {
            case "add":
                result = add(opKeyValue[1], opKeyValue[2]);
                LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);
                break;
            case "remove":
                result = remove(opKeyValue[1]);
                LOG.info("{}:{} {} {}", role, getId(), opKeyValue[0], opKeyValue[1]);
                break;
            default:
                result = opKeyValue[0] + ":invalid_operation";
                LOG.info("{}:{} {}", role, getId(), opKeyValue[0]);
        }

        final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));

        if (LOG.isTraceEnabled()) {
            LOG.trace("{}: key/values={}", getId(), key2values);
        }
        return f;
    }

    private String get(String key) {
        return key + "|" + key2values.get(key);
    }

    private String getAll() {
        return key2values.keySet().stream()
            .map(key -> key + "=" + key2values.get(key))
            .collect(Collectors.joining("|", "", ""));
    }

    private String add(String key, String val) {
        if (val.endsWith("\n")) {
            val = val.substring(val.length() - 1);
        }
        key2values.put(key, val);
        return "add|" + key + "|success";
    }

    private String remove(String key) {
        key2values.remove(key);
        return "remove|" + key + "|success";
    }
}
