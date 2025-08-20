package edu.npic.sps.features.report;

import edu.npic.sps.domain.User;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class GenerateReportServiceImpl implements GenerateReportService {

    @Override
    public File generateExcelReport(List<?> objects, String template) throws IOException {
        File excelFile = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");

        try (FileOutputStream fos = new FileOutputStream(excelFile)) {

            // Load JRXML template and compile
            final JasperReport report = loadTemplate(template);

            // Convert List to JRBeanCollectionDataSource
            final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);

            // Map to hold Jasper report Parameters
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBeanParam", dataSource);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

            // Export to XLSX
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fos));

            // Config
            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setOnePagePerSheet(false);
            config.setDetectCellType(true);
            config.setCollapseRowSpan(false);
            exporter.setConfiguration(config);

            exporter.exportReport();

            return excelFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File generatePDFReport(List<?> objects, String template) throws IOException {
        File pdfFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf");

        try (FileOutputStream pos = new FileOutputStream(pdfFile)) {

            // Load JRXML template and compile
            final JasperReport report = loadTemplate(template);

            // Convert List to JRBeanCollectionDataSource
            final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);

            // Map to hold Jasper report Parameters
            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBeanParam", dataSource);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

            // Export directly to PDF stream
            JasperExportManager.exportReportToPdfStream(jasperPrint, pos);

            return pdfFile;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JasperReport loadTemplate(String template) throws JRException {
        try (InputStream reportInputStream = getClass().getResourceAsStream(template)) {

            return (JasperReport) JRLoader.loadObject(reportInputStream);

        } catch (IOException e) {
            throw new JRException("Error loading report template", e);
        }

    }

}
