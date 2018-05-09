package com.softjourn.dev.quorumtutorial;

import java.math.BigInteger;
import java.util.Arrays;

import org.web3j.protocol.http.HttpService;

import org.web3j.quorum.*;
import org.web3j.quorum.methods.response.*;
import org.web3j.quorum.tx.ClientTransactionManager;

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

public class Main {

    // public address for private transactions between nodes
    private static String node1 = "J14OtqQK1lhaxV90XdbfZkzispEDK2FpyZ4sLFaAUgw=";
    private static String node2 = "Lm2d5O+d43WIxxY0aUt4IGiss0jnwC0qtrkH/Wpvilk=";
    // TransactionHash: 0x3924c73855d5bf1545a19e9ba9fa4a767f044997241aa3fd6946972cf1fe7626
    // contractAddress: 0x6ad18a85be5c1abd960c84ff2f3d0de066279f7d
    //                  0xa382023dbd253e728482b21bf2ccd5a3b720b2cd

    public static void main(String[] args) {
	    // write your code here
        System.out.println("Quorum tutorial");
        try {
            Quorum quorum = Quorum.build(new HttpService("http://localhost:22000"));
            QuorumNodeInfo quorumNodeInfo = quorum.quorumNodeInfo().send();
            String voteAccount = quorumNodeInfo.getNodeInfo().getVoteAccount();

            System.out.println(voteAccount);

            ClientTransactionManager transactionManager = new ClientTransactionManager(
                    quorum, voteAccount, Arrays.asList(node2)); //, node1));

            String address = deploy(quorum, transactionManager);
            System.out.println("Contract address: " + address); //*/
            //String address = "0x6ad18a85be5c1abd960c84ff2f3d0de066279f7d";
            System.out.println("get: " +  get(quorum, transactionManager, address));
            System.out.println("set(x): " +
                    set(quorum, transactionManager, address, BigInteger.valueOf(5)));
            System.out.println("get(): " + get(quorum, transactionManager, address));

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static String deploy(Quorum quorum, ClientTransactionManager transactionManager) throws Exception {
        BigInteger initVal = BigInteger.valueOf(42);
        Simplestorage contract = Simplestorage.deploy(
                quorum, transactionManager, GAS_PRICE, GAS_LIMIT, initVal).send();
        return contract.getContractAddress();
    }

    public static String get(Quorum quorum, ClientTransactionManager transactionManager, String address) throws Exception {
        Simplestorage contract = Simplestorage.load(
                address, quorum, transactionManager, GAS_PRICE, GAS_LIMIT);
        return contract.get().send().toString();
    }

    public static String set(Quorum quorum, ClientTransactionManager transactionManager, String address, BigInteger x) throws Exception {
        Simplestorage contract = Simplestorage.load(
                address, quorum, transactionManager, GAS_PRICE, GAS_LIMIT);
        return contract.set(x).send().toString();
    }
}
