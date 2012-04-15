package pds.p2p.node.webshell.webpages.node;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pds.p2p.api.node.client.DanubeApiClient;
import pds.p2p.node.webshell.webpages.BasePage;

public class Connection extends BasePage {

	private static final long serialVersionUID = -4101890723769407353L;

	protected static Logger log = LoggerFactory.getLogger(Connection.class.getName());

	public Connection() {

		this.setTitle(this.getString("title"));

		// create and add components

		this.add(new ConnectForm("connectForm", new CompoundPropertyModel<Connection> (this)));
		this.add(new DisconnectForm("disconnectForm", new CompoundPropertyModel<Connection> (this)));
	}

	private class ConnectForm extends Form<Connection> {

		private static final long serialVersionUID = -2720901123859278741L;

		private ConnectForm(String id, IModel<Connection> model) {

			super(id, model);
		}

		@Override
		protected void onSubmit() {

			// connect to vega

			Connection.log.debug("Connecting");

			try {

				DanubeApiClient.vegaObject.connect(null, null, null, null, null);
			} catch (Exception ex) {

				log.warn(ex.getMessage(), ex);
				error(Connection.this.getString("fail") + ex.getMessage());
				return;
			}

			// done

			info(Connection.this.getString("connected"));
		}
	}

	private class DisconnectForm extends Form<Connection> {

		private static final long serialVersionUID = -6253657678202734856L;

		private DisconnectForm(String id, IModel<Connection> model) {

			super(id, model);
		}

		@Override
		protected void onSubmit() {

			// disconnect from vega

			Connection.log.debug("Disconnecting");

			try {

				DanubeApiClient.vegaObject.disconnect();
			} catch (Exception ex) {

				log.warn(ex.getMessage(), ex);
				error(Connection.this.getString("fail") + ex.getMessage());
				return;
			}

			// done

			info(Connection.this.getString("disconnected"));
		}
	}
}
