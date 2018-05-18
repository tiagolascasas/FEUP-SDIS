package paxos;

import paxos.communication.Member;

import java.io.*;
import java.util.*;

public class PaxosUtils {
    static Member selectLeader(Collection<Member> members) {
        Member leader = null;
        for (Member member : members) {
            if (leader == null) {
                leader = member;
            } else {
                if (member.compareTo(leader) > 0) {
                    leader = member;
                }
            }
        }
        return leader;
    }

    public static byte[] serialize(Serializable serializable) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(serializable);
            out.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Object deserialize(byte[] bytes) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (ois != null) try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long findMax(Set<Long> longs) {
        long max = -1;
        for (Long n : longs) max = Math.max(n, max);
        return max;
    }
}
