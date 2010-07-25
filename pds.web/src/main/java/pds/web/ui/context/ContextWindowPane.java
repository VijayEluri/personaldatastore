package pds.web.ui.context;

import java.util.ResourceBundle;

import nextapp.echo.app.Extent;
import nextapp.echo.app.WindowPane;
import pds.web.PDSApplication;
import pds.web.events.ApplicationContextClosedEvent;
import pds.web.events.ApplicationContextOpenedEvent;
import pds.web.events.ApplicationEvent;
import pds.web.events.ApplicationListener;
import pds.web.ui.context.ClosedContentPane;

public class ContextWindowPane extends WindowPane implements ApplicationListener {

	private static final long serialVersionUID = -2629138014392846780L;

	protected ResourceBundle resourceBundle;

	/**
	 * Creates a new <code>AccountWindowPane</code>.
	 */
	public ContextWindowPane() {
		super();

		// Add design-time configured components.
		initComponents();
	}

	@Override
	public void init() {

		// put us in the bottom right corner

		this.setPositionX(new Extent(99999));
		this.setPositionY(new Extent(99999));

		// add us as listener

		PDSApplication.getApp().addApplicationListener(this);
	}

	@Override
	public void dispose() {

		// remove us as listener

		PDSApplication.getApp().removeApplicationListener(this);
	}

	public void onApplicationEvent(ApplicationEvent applicationEvent) {

		if (applicationEvent instanceof ApplicationContextClosedEvent) {

			this.removeAll();
			ClosedContentPane closedContentPane = new ClosedContentPane();
			this.add(closedContentPane);
		}

		if (applicationEvent instanceof ApplicationContextOpenedEvent) {

			this.removeAll();
			OpenContentPane openContentPane = new OpenContentPane();
			openContentPane.setContext(((ApplicationContextOpenedEvent) applicationEvent).getContext());
			this.add(openContentPane);
		}
	}

	/**
	 * Configures initial state of component.
	 * WARNING: AUTO-GENERATED METHOD.
	 * Contents will be overwritten.
	 */
	private void initComponents() {
		this.setStyleName("Glass");
		this.setTitle("Open / Close");
		this.setHeight(new Extent(240, Extent.PX));
		this.setMaximizeEnabled(false);
		this.setMinimizeEnabled(false);
		this.setClosable(false);
		this.setMinimumWidth(new Extent(450, Extent.PX));
		this.setWidth(new Extent(450, Extent.PX));
		this.setMinimumHeight(new Extent(240, Extent.PX));
		ClosedContentPane loginContentPane1 = new ClosedContentPane();
		add(loginContentPane1);
	}
}
