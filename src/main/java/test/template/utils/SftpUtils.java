package test.template.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SftpUtils {
	public final static int SFTP_PORT = 22;
	
	public static void put(String username, String password, String host, InputStream is, String filepath) throws Exception {
		Session session = null;
		ChannelSftp channel = null;
		
		JSch jsch = new JSch();
		try {
			//jsch.addIdentity("id_rsa");
			session = jsch.getSession(username, host, SFTP_PORT);
			session.setPassword(password);
			
			// StrictHostKeyChecking: ask | yes | no
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
	
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();

			channel.put(is, filepath, ChannelSftp.OVERWRITE);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (channel != null && channel.isConnected()) {
				channel.disconnect();
			}
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
		}
	}
	
	public static byte[] getBytes(String username, String password, String host, String filenpath) throws Exception {
		Session session = null;
		ChannelSftp channel = null;
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(username, host, SFTP_PORT);
			session.setPassword(password);
	
			// StrictHostKeyChecking: ask | yes | no
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
	
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			channel.get(filenpath, bos);
			return bos.toByteArray();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (channel != null && channel.isConnected()) {
				channel.disconnect();
			}
			if (session != null && session.isConnected()) {
				session.disconnect();
			}
		}
	}
}
