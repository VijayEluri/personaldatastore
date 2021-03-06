package pds.p2p.node.webshell.objects;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Intent extends Packet {

	private static final long serialVersionUID = -3734622557993328577L;

	private static Logger log = LoggerFactory.getLogger(Intent.class.getName());

	private String intentId;
	private String item;
	private String price;

	public Intent() {

		this.intentId = createId();
	}

	public String getIntentId() {

		return this.intentId;
	}

	public void setIntentId(String intentId) {

		this.intentId = intentId;
	}

	public String getItem() {

		return this.item;
	}

	public void setItem(String item) {

		this.item = item;
	}

	public String getPrice() {

		return this.price;
	}

	public void setPrice(String price) {

		this.price = price;
	}

	@Override
	public void fromPacket (String rawpacket) throws UnsupportedEncodingException, JSONException {

		super.fromPacket(rawpacket);

		this.fromContent(this.getContent());
	}

	public void fromContent(String content) throws JSONException, UnsupportedEncodingException {

		log.debug("content: " + content);

		JSONObject contentObject = new JSONObject(content);

		this.intentId = contentObject.has("intentid") ? contentObject.getString("intentid") : null;
		this.item = contentObject.has("item") ? contentObject.getString("item") : null;
		this.price = contentObject.has("price") ? contentObject.getString("price") : null;
	}

	public String toJSON() {

		StringBuffer json = new StringBuffer();
		json.append("{");
		json.append("\"intentid\":\"" + this.intentId.replace("\"", "\\\"") + "\"");
		json.append(",");
		json.append("\"item\":\"" + this.item.replace("\"", "\\\"") + "\"");
		json.append(",");
		json.append("\"price\":\"" + this.price.replace("\"", "\\\"") + "\"");
		json.append("}");

		return json.toString();
	}

	@Override
	public String toString() {

		return "#" + this.intentId + ": " + this.item + " (" + this.price + ")";
	}

	@Override
	public boolean equals(Object object) {

		if (this == object) return true;
		if (! (object instanceof Intent)) return false;

		Intent other = (Intent) object;

		if (! this.intentId.equals(other.intentId)) return false;
		if (! this.item.equals(other.item)) return false;
		if (! this.price.equals(other.price)) return false;

		return true;
	}

	@Override
	public int hashCode() {

		int hashCode = 1;

		hashCode += hashCode * 31 + this.intentId.hashCode();
		hashCode += hashCode * 31 + this.item.hashCode();
		hashCode += hashCode * 31 + this.price.hashCode();

		return hashCode;
	}

	private static String createId() {

		final Random random = new Random();
		final String hex = "0123456789abcdef";

		StringBuilder id = new StringBuilder();
		for (int i=0; i<8; i++) id.append(hex.charAt(random.nextInt(hex.length())));

		return id.toString();
	}
}
