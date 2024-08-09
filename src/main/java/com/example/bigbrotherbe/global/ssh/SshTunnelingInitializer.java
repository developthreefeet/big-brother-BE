//package com.example.bigbrotherbe.global.ssh;
//
//
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//import jakarta.annotation.PreDestroy;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.annotation.Validated;
//
//@Slf4j
//@Component
//@Validated @Setter
//public class SshTunnelingInitializer {
//
//    @Value("${ssh.remote-host}")
//    private String remoteJumpHost;
//
//    @Value("${ssh.ssh-port}")
//    private int sshPort;
//
//    @Value("${ssh.user-name}")
//    private String userName;
//
//    @Value("${ssh.private-key-path}")
//    private String privateKeyPath; // SSH Private Key 경로;
//    private int databasePort = 3306;
//
//    private Session session;
//    @PreDestroy
//    public void destroy() {
//        if (session.isConnected())
//            session.disconnect();
//    }
//
//    public Integer buildSshConnection() {
//        Integer forwardPort = null;
//
//        try {
//            log.info("Connecting to SSH with {}@{}:{} using privateKey at {}", userName, remoteJumpHost, sshPort, privateKeyPath);
//            JSch jsch = new JSch();
//
//            // connection 1. application server to jump server 앱서버 -> ec2
//            jsch.addIdentity(privateKeyPath); // pem 키 추가
//            session = jsch.getSession(userName, remoteJumpHost, sshPort); // 세션 설정
//            session.setConfig("StrictHostKeyChecking", "no");
//
//            log.info("Starting SSH session connection...");
//            session.connect(); // 연결
//            log.info("SSH session connected");
//
//            // connection 2. jump server to remote server
//            forwardPort = session.setPortForwardingL(0, "localhost", databasePort);
//            log.info("Port forwarding created on local port {} to remote port {}", forwardPort, databasePort);
//
//        } catch (JSchException e) {
//            log.error(e.getMessage());
//            this.destroy();
//            throw new RuntimeException(e);
//        }
//
//        return forwardPort;
//    }
//}