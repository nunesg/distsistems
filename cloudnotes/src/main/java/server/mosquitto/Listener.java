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

import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import cloudnotes.server.ListenerCallback;

/**
 * The Class Listner.
 * 
 * @author Yasith Lokuge
 */
public class Listener implements MqttCallback {
  private static final String brokerUrl = "tcp://localhost:2222";
  private String clientId;
  private MqttClient client;
  private HashMap<String, ListenerCallback> callbacks;

  public Listener(String id) {
    try {
      this.clientId = id;
      this.callbacks = new HashMap<String, ListenerCallback>();
      client = new MqttClient(brokerUrl, clientId, new MemoryPersistence());
      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);

      System.out.println("Creating listener: " + clientId);

      System.out.println("Mqtt Connecting to broker: " + brokerUrl);
      client.connect(connOpts);
      System.out.println("Mqtt Connected");

      client.setCallback(this);
    } catch (MqttException e) {
      System.out.println("Exception while constructing listener: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public String getId() {
    return clientId;
  }

	public void subscribe(String topic, ListenerCallback callback) {
    System.out.println("Subscribing to topic: " + topic);
		try {
			client.subscribe(topic);
      callbacks.put(topic, callback);
			System.out.println("Subscribed");
		} catch (Exception e) {
      System.out.println("Exception while subscribing to topic " + topic + ". Message: " + e.toString());
      e.printStackTrace();
		}
	}

	public void connectionLost(Throwable arg0) {}

	public void deliveryComplete(IMqttDeliveryToken arg0) {}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
    System.out.println("\nMessage arrived\nMqtt topic : " + topic);
		System.out.println("Mqtt msg : " + message.toString());
    callbacks.get(topic).run(message.getPayload());
	}

}