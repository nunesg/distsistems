/*******************************************************************************
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cloudnotes.server.mosquitto;

import java.lang.AutoCloseable;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Publisher implements AutoCloseable {
  private final String brokerUrl = "tcp://localhost:2222";
  private String clientId;
  private MqttClient client;

  public Publisher(String id) {
    try {
      this.clientId = id;
      this.client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);

      System.out.println("Creating publisher: " + clientId);
      System.out.println("Connecting to broker: " + brokerUrl);
      client.connect(connOpts);
      System.out.println("Connected");
    } catch (MqttException e) {
      System.out.println("Exception on publisher constructor: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void publish(String topic, byte[] message) {
    publish(topic, message, 2);
  }

  public void publish(String topic, byte[] msg, int qos) {
    try{
      MqttMessage message = new MqttMessage(msg);
      message.setQos(qos);
      client.publish(topic, message);
      System.out.println("Message: {" + message.toString() + "} published");
    } catch (MqttException e) {
      System.out.println("Exception while publishing: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      client.disconnect();
		  System.out.println("Disconnected");
    } catch (MqttException e) {
      System.out.println("Error trying to disconnect client.");
    }
  }
}