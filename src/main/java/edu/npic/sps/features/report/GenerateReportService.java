package edu.npic.sps.features.report;

import edu.npic.sps.domain.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface GenerateReportService {



    File generateExcelReport(List<?> objects, String template) throws IOException;

    File generatePDFReport(List<?> objects, String template) throws IOException;
}
