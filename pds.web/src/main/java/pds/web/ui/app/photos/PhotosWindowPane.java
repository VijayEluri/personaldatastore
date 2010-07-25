package pds.web.ui.app.photos;

import java.util.ResourceBundle;

import nextapp.echo.app.Extent;
import nextapp.echo.app.WindowPane;

import org.eclipse.higgins.xdi4j.xri3.impl.XRI3Segment;

import pds.web.xdi.objects.XdiContext;

public class PhotosWindowPane extends WindowPane {

	private static final long serialVersionUID = 4111493581013444404L;

	protected ResourceBundle resourceBundle;

	private PhotosContentPane addressBookContentPane;

	/**
	 * Creates a new <code>ConfigureAPIsWindowPane</code>.
	 */
	public PhotosWindowPane() {
		super();

		// Add design-time configured components.
		initComponents();
	}

	public void setContextAndSubjectXri(XdiContext context, XRI3Segment subjectXri) {

		this.addressBookContentPane.setContextAndSubjectXri(context, subjectXri);
	}

	/**
	 * Configures initial state of component.
	 * WARNING: AUTO-GENERATED METHOD.
	 * Contents will be overwritten.
	 */
	private void initComponents() {
		this.setStyleName("Blue");
		this.setTitle("Address Book");
		this.setHeight(new Extent(600, Extent.PX));
		this.setMinimizeEnabled(false);
		this.setMaximizeEnabled(true);
		this.setClosable(true);
		this.setWidth(new Extent(800, Extent.PX));
		addressBookContentPane = new PhotosContentPane();
		add(addressBookContentPane);
	}
}
