package com.hancom.hanzari.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.hancom.hanzari.vo.PlacementVo;

public class CSVHelper {
	public static String TYPE = "text/csv";

	public static String[] HEADERs = { "Floor", "SeatName", "EmployeeId" };

	public static boolean hasCSVFormat(MultipartFile file) {
		// TODO 여기서 CSV 파일인지 타입체크를 해주는 부분을 넣어줘야 할 것 같습니다.
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<PlacementVo> csvToSeat(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<PlacementVo> placementVos = new ArrayList<PlacementVo>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				placementVos.add(PlacementVo.builder().floor(csvRecord.get("Floor")).seatName(csvRecord.get("SeatName"))
						.employeeId(csvRecord.get("EmployeeId")).build());
			}

			return placementVos;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}
}