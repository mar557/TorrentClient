public class Peer {
	/** The peer id. */
	protected byte[] peerId;
	
	/** The port. */
	protected int port;
	
	/** The ip. */
	protected String ip;
	public Peer(byte[] peerId, int port, String ip) {
		this.peerId = peerId;
		this.port = port;
		this.ip = ip;
	}
	
	public byte[] getpeerId() {
        return peerId;
    }
	
	public int getport() {
        return port;
    }
	
	public String getip() {
        return ip;
    }
}


	