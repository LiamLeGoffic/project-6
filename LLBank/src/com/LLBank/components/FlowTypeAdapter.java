package com.LLBank.components;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

// 2.1 JSON file of flows
public class FlowTypeAdapter extends TypeAdapter<Flow> {

	@Override
	public void write(JsonWriter out, Flow flow) throws IOException {
	}

	@Override
	public Flow read(JsonReader in) throws IOException {
		in.beginObject();

		String type = "";

		String comment = null;
		double amount = 0.0;
		Long targetAccountNumber = null;
		boolean effect = false;
		LocalDate dateOfFlow = null;
		Long issuierAccountNumberLong = null;

		while (in.hasNext()) {
			String name = in.nextName();
			switch (name) {
			case "type":
				type = in.nextString();
				break;
			case "comment":
				comment = in.nextString();
				break;
			case "amount":
				amount = in.nextDouble();
				break;
			case "targetAccountNumber":
				targetAccountNumber = in.nextLong();
				break;
			case "effect":
				effect = in.nextBoolean();
				break;
			case "dateOfFlow":
				String dateStr = in.nextString(); // Lit la date sous forme de chaîne
				dateOfFlow = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				break;
			case "issuierAccountNumber":
				issuierAccountNumberLong = in.nextLong();
				break;
			default:
				in.skipValue(); // Ignore les valeurs inconnues
			}
		}
		in.endObject();

		Flow flow = null;
		switch (type) {
		case "Debit":
			flow = new Debit(comment, amount, targetAccountNumber, effect, dateOfFlow);
			break;
		case "Credit":
			flow = new Credit(comment, amount, targetAccountNumber, effect, dateOfFlow);
			break;
		case "Transfert":
			flow = new Transfert(comment, amount, targetAccountNumber, effect, dateOfFlow, issuierAccountNumberLong);
			break;
		default:
			throw new IllegalArgumentException("Unknown type: " + type);
		}

		return flow;
	}
}
