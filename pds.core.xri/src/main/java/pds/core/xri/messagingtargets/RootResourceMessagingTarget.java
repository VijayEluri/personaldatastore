package pds.core.xri.messagingtargets;

import pds.core.xri.XriPdsInstanceFactory;
import xdi2.core.exceptions.Xdi2MessagingException;
import xdi2.messaging.target.impl.ResourceHandler;
import xdi2.messaging.target.impl.ResourceMessagingTarget;

public class RootResourceMessagingTarget extends ResourceMessagingTarget {

	private XriPdsInstanceFactory pdsInstanceFactory;

	public RootResourceMessagingTarget() {

		super(true);
	}

	@Override
	public ResourceHandler getResource(Message message, Subject subject) throws Xdi2MessagingException {

		if (message.getMessageEnvelope().getGraph().containsStatement(subject.getSubjectXri(), DictionaryConstants.XRI_IS_A, new XRI3Segment(subject.getSubjectXri().toString().substring(0, 1)))) {

			return new RootSubjectResourceHandler(message, subject, this.pdsInstanceFactory);
		}

		return null;
	}

	public XriPdsInstanceFactory getPdsInstanceFactory() {

		return this.pdsInstanceFactory;
	}

	public void setPdsInstanceFactory(XriPdsInstanceFactory pdsInstanceFactory) {

		this.pdsInstanceFactory = pdsInstanceFactory;
	}
}
