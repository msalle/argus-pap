package org.glite.authz.pap.monitoring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MonitoringServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException , IOException {
    
        MonitoredProperties props = MonitoredProperties.instance();
        resp.getWriter().print(props.toString());       
        resp.setStatus( 200 );
    }

}