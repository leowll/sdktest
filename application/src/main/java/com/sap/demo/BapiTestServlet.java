package com.sap.demo;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.BapiRequest;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.BapiRequestResult;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.Table;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.TableRow;

/**
 * Servlet implementation class BapiTestServlet
 */
@WebServlet("/BapiTestServlet")
public class BapiTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String destinationName = "ErpQueryEndpoint";
		Destination destination = DestinationAccessor.getDestination(destinationName);
		PrintWriter writer = response.getWriter();
		try {
			BapiRequestResult result = getAccountGL(destination);
			writer.write(result.size());
		} catch (Throwable e) {
			e.printStackTrace(writer);
		}

	}

	private BapiRequestResult getAccountGL(Destination destination) throws Throwable {
		final BapiRequest request = new BapiRequest("BAPI_ACC_DOCUMENT_CHECK", true);
		request.withExporting("DOCUMENTHEADER", "BAPIACHE09");
		request.withTableAsReturn("BAPIRET2");
		
		Table<BapiRequest> accountGLTable = request.withTable("ACCOUNTGL", "BAPIACGL09");
		final TableRow<BapiRequest> row = accountGLTable.row();
		row.field("ACCT_KEY", "KOART", "121");

		return request.execute(destination);
	}

}
