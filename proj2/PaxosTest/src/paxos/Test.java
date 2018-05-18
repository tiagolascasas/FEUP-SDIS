package paxos;

import java.io.Serializable;
import java.net.InetAddress;

import paxos.communication.Member;
import paxos.communication.Members;

public class Test {

	public static void main(String[] args) {
		Test test = new Test();
		try {
			test.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void run() throws Exception{
        Members members = new Members(
                new Member(), // this is a reference to a member on the localhost on default port (2440)
                new Member(2441), // this one is on localhost with the specified port
                new Member(InetAddress.getLocalHost(), 2442)); // you can specify the address and port manually

        // we need to define a receiver
        class MyReceiver implements Receiver {
            // we follow a reactive pattern here
            public void receive(Serializable message) {
                System.out.println("received " + message.toString());
            }
        };

        // this actually creates the members
        BasicGroup group1 = new BasicGroup(members.get(0), new MyReceiver());
        BasicGroup group2 = new BasicGroup(members.get(1), new MyReceiver());
        BasicGroup group3 = new BasicGroup(members.get(2), new MyReceiver());

        // this will cause all receivers to print "received Hello"
        group2.broadcast("Hello");

        Thread.sleep(1); // allow the members to receive the message

        group1.close(); group2.close(); group3.close();
	}
}
