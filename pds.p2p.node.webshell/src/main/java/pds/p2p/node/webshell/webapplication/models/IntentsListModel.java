package pds.p2p.node.webshell.webapplication.models;

import java.util.ArrayList;

import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pds.p2p.api.node.client.DanubeApiClient;
import pds.p2p.node.webshell.objects.Intent;

public class IntentsListModel implements IModel<ArrayList<Intent>> {

	private static final long serialVersionUID = 5241414033305685135L;

	private static Logger log = LoggerFactory.getLogger(IntentsListModel.class.getName());

	private ArrayList<Intent> intents;

	@Override
	public void detach() {

		this.intents = null;
	}

	public ArrayList<Intent> getObject() {

		if (this.intents != null) return this.intents;

		log.debug("Getting intents.");

		// get XDI data

		String[] rawpackets;

		try {

			String inumber = DanubeApiClient.orionObject.inumber();
			rawpackets = DanubeApiClient.polarisObject.getLiterals(inumber + "+intent");
		} catch (Exception ex) {

			throw new RuntimeException(ex.getMessage(), ex);
		}

		// prepare intents

		this.intents = new ArrayList<Intent> ();

		for (String rawpacket : rawpackets) {

			try {

				Intent intent = new Intent();
				intent.fromPacket(rawpacket);

				this.intents.add(intent);
			} catch (Exception ex) {

				throw new RuntimeException(ex.getMessage(), ex);
			}
		}

		// done

		log.debug("Loaded " + this.intents.size() + " intents.");

		return this.intents;
	}

	@Override
	public void setObject(ArrayList<Intent> object) {

		throw new RuntimeException("setObject() not supported");
	}

}
