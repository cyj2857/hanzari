package com.hancom.hanzari.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.hancom.hanzari.model.Seat;

public class CSVHelper {
	public static String TYPE = "text/csv";
	static String[] HEADERs = { "seatName", "employeeId" };

	public static boolean hasCSVFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<Seat> csvToSeat(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<Seat> seats = new ArrayList<Seat>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			// 여기서 seatDto로 변경하거나 하자
			
			for (CSVRecord csvRecord : csvRecords) {
				String seatName = csvRecord.get("SeatName");
				String employeeId =  csvRecord.get("employeeId");

			}
			
			/*return tutorials;*/
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
		return null;
	}

}