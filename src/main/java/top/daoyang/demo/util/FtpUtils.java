package top.daoyang.demo.util;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;


@Slf4j
@Component
public class FtpUtils {

    public static void upload(String host, String fileName, InputStream inputStream, String username, String password, String uploadFold) throws SftpException, JSchException {
        upload2(host, 22, username, password, fileName, inputStream, uploadFold);
    }

    private static void upload(String host, int port, String username, String password, String fileName, InputStream inputStream) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReply())) {
                log.info("Can't connect to ftp server, password or username error");
                ftpClient.disconnect();
            }
            ftpClient.changeWorkingDirectory("alipayQRCode/");
            if (ftpClient.storeFile(fileName, inputStream)) {
                inputStream.close();
                ftpClient.logout();

                log.info("Uploading success");
            }
        } catch (IOException e) {
            log.info("FTP connect error");
            log.error(e.getMessage());
        }
    }

    private static void upload2(String host, int port, String username, String password, String fileName, InputStream inputStream, String uploadFold) throws JSchException {
        ChannelSftp sftp = null;
        JSch jsch = new JSch();
        Session sshSession = jsch.getSession(username, host, port);
        sshSession.setPassword(password);
        sshSession.setConfig("StrictHostKeyChecking", "no");
        try {
            sshSession.connect();
        } catch (JSchException e) {
            log.error("Connecting fail");
            log.info(e.getMessage());
        }
        Channel channel = null;
        try {
            channel = sshSession.openChannel("sftp");
        } catch (JSchException e) {
            log.error("Opening channel fail");
            log.info(e.getMessage());
        }
        try {
            channel.connect();
        } catch (JSchException e) {
            log.error("Connecting channel fail");
            log.info(e.getMessage());
        }
        sftp = (ChannelSftp) channel;

        try {
            sftp.cd(uploadFold);
        } catch (SftpException e) {
            log.error("CD fail");
            log.info(e.getMessage());
        }
        try {
            sftp.put(inputStream, fileName);
            log.info("sftp location {}", sftp.pwd());
        } catch (SftpException e) {
            log.error("Uploading file fail");
            log.info(e.getMessage());
        } finally {
            try {
                inputStream.close();
                sshSession.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
