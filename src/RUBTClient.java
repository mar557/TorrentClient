import java.io.File;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import GivenTools.Bencoder2;
import GivenTools.BencodingException;
import GivenTools.TorrentInfo;

/**
 * 
 */

/**
 * 
 * 
 * 
 * @author Michael Reid
 * @author Stacey Mui
 *
 */
public class RUBTClient extends Thread
{

	/**
	 * Logger for the local client.
	 */
	private final static Logger LOGGER = Logger.getLogger(RUBTClient.class.getName());

e	public static void main(String[] args) 
	{
		//Set level of logger to INFO or higher
		LOGGER.setLevel(Level.INFO);

		// Check number/type of arguments
		if (args.length != 2) 
		{
			LOGGER.log(Level.SEVERE, "Two arguments required. Exiting program");
			System.exit(1);
		}

		String torrentChecker = args[0].substring(args[0].lastIndexOf(".") + 1, args[0].length());
		
		if (!(torrentChecker.equals("torrent"))) 
		{
			LOGGER.log(Level.SEVERE, "Not a valid .torrent file. Exiting program.");
			System.exit(1);
		}

		// Open torrent file
		byte[] metaBytes = null;
		try 
		{
			LOGGER.info("Opening torrent file...");
			File metaFile = new File(args[0]);
			DataInputStream metaFileIn = new DataInputStream(new FileInputStream(metaFile));
			metaBytes = new byte[(int)metaFile.length()];
			LOGGER.info("Reading torrent file");
			metaFileIn.readFully(metaBytes);
			metaFileIn.close();
			LOGGER.info("Closing torrent file");
		} 
		catch (FileNotFoundException fnfEx) 
		{
			LOGGER.log(Level.SEVERE, "File "  + args[0] + " not found.", fnfEx);
			System.exit(1);
		} 
		catch (IOException ioEx) 
		{
			LOGGER.log(Level.SEVERE, "I/O exception for file " + args[0], ioEx);
			System.exit(1);
		}

		// Null check on metaBytes
		if (metaBytes == null) 
		{
			LOGGER.log(Level.SEVERE, "Corrupt torrent file.");
			System.exit(1);
		}

		// Decode torrent file
		TorrentInfo tInfo = null;
		try 
		{
			LOGGER.info("Decoding file...");
			tInfo = new TorrentInfo(metaBytes);
			
			getListOfPeersHttpUrl(tInfo.announce_url);

		} 
		catch (BencodingException be) 
		{
			LOGGER.log(Level.WARNING, "Bencoding exception", be);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	private static URL makeKey(TorrentInfo ti) throws URISyntaxException, MalformedURLException 
	{
		byte[] hash = ti.info_hash.array();
		URI uri = ti.announce_url.toURI();
		String stringKey = uri.getPath() + "%3Finfo_hash%3D";
		for(byte b : hash)
			stringKey = stringKey + "%" + String.format("%02X", b);
		stringKey = stringKey + "%26peer_id%3D" + makePeerID();
	    URI newUri = uri.resolve(stringKey);
	    return newUri.toURL();
	}
	    
	//public void getRequest() 
		//String key, line, stringResponse;

	private static String makePeerID() {
		// TODO Auto-generated method stub
		return null;
	}
	private static Object getListOfPeersHttpUrl(URL trackerURL) throws IOException, BencodingException
	{
		LOGGER.info("Getting list of Peers");
		byte[] byteResponse;
		String line, stringResponse;
		StringBuffer response = null;
		HttpURLConnection connection = (HttpURLConnection)trackerURL.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while((line = reader.readLine()) != null)
			response.append(line);
		reader.close();
		stringResponse = response.toString();
		byteResponse = stringResponse.getBytes();
		Object peerList = Bencoder2.decode(byteResponse);
		return peerList;
	}
	private static byte[] generatePeerId() 
	{
		final byte[] peerId = new byte[20];
		//LOGGER.info("Adding RU to ID" );
		//peerId[0] = 'R';
		//peerId[1] = 'U';
		
		//final byte[] remainPeerId = new byte[18];
		LOGGER.info("Generating 20 Random bytes");
		new Random().nextBytes(peerId);
		//System.arraycopy(remainPeerId, 0, peerId, 2, remainPeerId.length);
		return peerId;
	}
	byte[] byteResponse;
	String line, stringResponse;
	StringBuffer response = null;
	URL key = makeKey(tInfo);
	HttpURLConnection connection = (HttpURLConnection)key.openConnection();
	connection.setRequestMethod("GET");
	DataInputStream dis = new DataInputStream(connection.getInputStream());
	//notetoself: change variable names to avoid plagarism
	int dataSize =connection.getContentLength();
	byte[] retArray = new byte[dataSize];
	dis.readFully(retArray);                             
	
	/*while((line = reader.readLine()) != null)
		response.append(line);
	reader.close();
	stringResponse = response.toString();
	byteResponse = stringResponse.getBytes();*/
	@SuppressWarnings("unchecked")
	//Map<ByteBuffer, Object> peerList = (Map<ByteBuffer,Object>) Bencoder2.decode(retArray);
	Object peerList = Bencoder2.decode(retArray);          
	System.out.println(peerList.toString());                                                                                       //The Code works up to here!
	ArrayList<Peer> peers = new ArrayList<Peer>();                      //Not sure how to extract peer IP addresses

	this.interval = (Integer)response.get(KEY_INTERVAL);

	List<Map<ByteBuffer, Object>> peersList = (List<Map<ByteBuffer, Object>>) response.get(KEY_PEERS);
}

private static URL makeKey(TorrentInfo ti) throws URISyntaxException, MalformedURLException {
	byte[] hash = ti.info_hash.array();
	URI uri = ti.announce_url.toURI();
	String stringKey = uri.getPath() + "?info_hash=";
	for(byte b : hash)
		stringKey = stringKey + "%" + String.format("%02X", b);
	stringKey = stringKey + "&peer_id=" + generatePeerId() + "&port=6881&uploaded=0&downloaded=0&left=" + ti.file_length + "&event=started";
    URI newUri = uri.resolve(stringKey);
    return newUri.toURL();
}

This I worked on but never ran:
/*private static void peerHandshake() {
	BufferedReader br = new BufferedReader(new InputStreamReader(TCPSocket.getInputStream()));
	byte[] reserved = new byte[8];
	DataOutputStream os = new DataOutputStream(TCPSocket.getOutputStream());	
	Socket TCPSocket = new Socket();
	String protocolId = "BitTorrent Protocol", peerHandshake, peerResponse;
	
	Arrays.fill( reserved, (byte) 0 );
	os.writeByte(1);
	os.writeBytes(protocolId);
	os.write(reserved, 0, reserved.length());
	// write SHA1 hash and peerId
	peerHandshake = br.readLine();
	//Sending interested message
	out.writeByte( 1 );
	out.writeInt( 2 );
	peerResponse = br.readLine();
}*/
	
}

