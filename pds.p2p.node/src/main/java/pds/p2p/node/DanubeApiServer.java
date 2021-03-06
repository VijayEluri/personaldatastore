package pds.p2p.node;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pds.p2p.api.Admin;
import pds.p2p.api.Orion;
import pds.p2p.api.Polaris;
import pds.p2p.api.Sirius;
import pds.p2p.api.Vega;
import pds.p2p.api.annotation.DanubeApi;
import pds.p2p.node.admin.AdminImpl;
import pds.p2p.node.servlets.MyJsonRpcServlet;

public class DanubeApiServer implements ServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(DanubeApiServer.class);

	private static final int DEFAULT_ECHOPORT = 15019;

	private static boolean initialized = false;
	private static ScriptThread loopScriptThread;
	private static EchoTcpThread echoTcpThread;
	private static EchoUdpThread echoUdpThread;

	public static Admin adminObject;
	public static Orion orionObject;
	public static Vega vegaObject;
	public static Sirius siriusObject;
	public static Polaris polarisObject;

	@Override
	public void contextInitialized(ServletContextEvent e) {

		if (initialized) throw new RuntimeException("Already initialized.");

		try {

			init(e.getServletContext());
			initialized = true;
		} catch (Throwable ex) {

			throw new RuntimeException(ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {

		if (! initialized) return;

		shutdown();
	}

	public static Collection<Class<?>> apiInterfaces() {

		return Arrays.asList(new Class<?>[] { Admin.class, Orion.class, Vega.class, Sirius.class, Polaris.class });
	}

	public static Class<?> apiInterface(String apiName) {

		for (Class<?> clazz : apiInterfaces()) {

			if (apiName.equals(clazz.getAnnotation(DanubeApi.class).name())) return clazz;
		}

		return null;
	}

	private static void init(ServletContext servletContext) throws Throwable {

		log.info("init()");

		// init threads

		loopScriptThread = new ScriptThread();
		echoTcpThread = new EchoTcpThread(DEFAULT_ECHOPORT);
		echoUdpThread = new EchoUdpThread(DEFAULT_ECHOPORT);

		// init API

		adminObject = new AdminImpl(new Date(), loopScriptThread);

		orionObject = pds.p2p.api.orion.OrionFactory.getOrion();
		if (pds.p2p.api.orion.OrionFactory.getException() != null) throw pds.p2p.api.orion.OrionFactory.getException();

		vegaObject = pds.p2p.api.vega.VegaFactory.getVega(orionObject);
		if (pds.p2p.api.vega.VegaFactory.getException() != null) throw pds.p2p.api.vega.VegaFactory.getException();

		siriusObject = pds.p2p.api.sirius.SiriusFactory.getSirius(vegaObject);
		if (pds.p2p.api.sirius.SiriusFactory.getException() != null) throw pds.p2p.api.sirius.SiriusFactory.getException();

		polarisObject = pds.p2p.api.polaris.PolarisFactory.getPolaris(orionObject, vegaObject);
		if (pds.p2p.api.polaris.PolarisFactory.getException() != null) throw pds.p2p.api.polaris.PolarisFactory.getException();

		adminObject.init();
		orionObject.init();
		vegaObject.init();
		siriusObject.init();
		polarisObject.init();

		// adding servlets

		log.info("Adding servlets...");

		servletContext.addServlet(Admin.class.getAnnotation(DanubeApi.class).name(), new MyJsonRpcServlet(adminObject)).addMapping("/" + Admin.class.getAnnotation(DanubeApi.class).name());
		servletContext.addServlet(Orion.class.getAnnotation(DanubeApi.class).name(), new MyJsonRpcServlet(orionObject)).addMapping("/" + Orion.class.getAnnotation(DanubeApi.class).name());
		servletContext.addServlet(Vega.class.getAnnotation(DanubeApi.class).name(), new MyJsonRpcServlet(vegaObject)).addMapping("/" + Vega.class.getAnnotation(DanubeApi.class).name());
		servletContext.addServlet(Sirius.class.getAnnotation(DanubeApi.class).name(), new MyJsonRpcServlet(siriusObject)).addMapping("/" + Sirius.class.getAnnotation(DanubeApi.class).name());
		servletContext.addServlet(Polaris.class.getAnnotation(DanubeApi.class).name(), new MyJsonRpcServlet(polarisObject)).addMapping("/" + Polaris.class.getAnnotation(DanubeApi.class).name());

		// start threads

		log.info("Starting threads...");

		loopScriptThread.start();
		echoTcpThread.start();
		echoUdpThread.start();
	}

	private static void shutdown() {

		log.info("shutdown()");

		// shutdown threads

		log.info("Stopping threads...");

		try {

			loopScriptThread.stopRunning();
			echoTcpThread.stopRunning();
			echoUdpThread.stopRunning();

			loopScriptThread.interrupt();
			echoTcpThread.interrupt();
			echoUdpThread.interrupt();
			
			loopScriptThread.join();
			echoTcpThread.join();
			echoUdpThread.join();
		} catch (Exception ex) {

			log.error(ex.getMessage(), ex);
		}

		// shutdown API

		adminObject.shutdown();
		polarisObject.shutdown();
		siriusObject.shutdown();
		vegaObject.shutdown();
		orionObject.shutdown();

		adminObject = null;
		polarisObject = null;
		siriusObject = null;
		vegaObject = null;
		orionObject = null;
	}
}
