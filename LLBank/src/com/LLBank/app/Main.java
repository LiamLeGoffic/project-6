package com.LLBank.app;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.LLBank.components.Account;
import com.LLBank.components.Client;
import com.LLBank.components.Credit;
import com.LLBank.components.CurrentAccount;
import com.LLBank.components.Debit;
import com.LLBank.components.Flow;
import com.LLBank.components.FlowTypeAdapter;
import com.LLBank.components.SavingsAccount;
import com.LLBank.components.Transfert;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Main {

	public static void main(String[] args) {
		// 1.1.2 Creation of main class for tests
		System.out.println("Question 1.1.2");
		List<Client> clients = generateClientSet(4);
		displayClients(clients);

		// 1.2.3 Creation of the tablea account
		System.out.println("Question 1.2.3");
		List<Account> accounts = generateAccountSet(clients);
		displayAccounts(accounts);

		// 1.3.1 Adaptation of the table of accounts
		System.out.println("Question 1.3.1");
		Hashtable<Long, Account> accountsHashtable = hashAccounts(accounts);
		displayAccountsHashtable(accountsHashtable);

		// 1.3.4 Creation of the flow array
		List<Flow> flows = generateFlows(accountsHashtable);

		// 1.3.5 Updating accounts
		System.out.println("Question 1.3.5");
		updateBalances(flows, accountsHashtable);
		displayAccountsHashtable(accountsHashtable);

		// 2.1 JSON file of flows
		System.out.println("Question 2.1");
		flows = fillFlowArrayWithJson();
		updateBalances(flows, accountsHashtable);
		displayAccountsHashtable(accountsHashtable);

		// 2.2 XML file of account
		System.out.println("Question 2.2");
		accounts = fillAccountArrayWithXml(clients);
		displayAccounts(accounts);

	}

	private static List<Client> generateClientSet(int n) {
		List<Client> generatedClients = new ArrayList<Client>();
		for (int i = 1; i <= n; i++) {
			generatedClients.add(new Client("name" + i, "firstname" + i));
		}
		return generatedClients;
	}

	private static void displayClients(List<Client> clients) {
		clients.stream().forEach(System.out::println);
	}

	private static List<Account> generateAccountSet(List<Client> clients) {
		List<Account> generatedAccounts = new ArrayList<Account>();
		for (int i = 0; i < clients.size(); i++) {
			generatedAccounts.add(new CurrentAccount("CurrentAccount" + (i + 1), clients.get(i)));
			generatedAccounts.add(new SavingsAccount("SavingsAccount" + (i + 1), clients.get(i)));
		}
		return generatedAccounts;
	}

	private static void displayAccounts(List<Account> accounts) {
		accounts.stream().forEach(System.out::println);
	}

	private static Hashtable<Long, Account> hashAccounts(List<Account> accounts) {
		Hashtable<Long, Account> hashtable = new Hashtable<>();
		for (Account account : accounts) {
			hashtable.put(account.getAccountNumber(), account);
		}
		return hashtable;
	}

	private static void displayAccountsHashtable(Hashtable<Long, Account> accountsHashtable) {
		accountsHashtable.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(
						(account1, account2) -> Double.compare(account1.getBalance(), account2.getBalance())))
				.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
	}

	private static List<Flow> generateFlows(Hashtable<Long, Account> accountsHashtable) {
		List<Flow> generatedFlows = new ArrayList<>();
		LocalDate flowsDate = LocalDate.now().plusDays(2);
		generatedFlows.add(new Debit("Debit1", 50, accountsHashtable.get(1L).getAccountNumber(), false, flowsDate));
		for (Account account : accountsHashtable.values()) {
			if (account instanceof CurrentAccount) {
				generatedFlows.add(new Credit("Credit" + account.getAccountNumber(), 100.5, account.getAccountNumber(),
						false, flowsDate));
			} else if (account instanceof SavingsAccount) {
				generatedFlows.add(new Credit("Credit" + account.getAccountNumber(), 1500, account.getAccountNumber(),
						false, flowsDate));
			}
		}
		generatedFlows.add(new Transfert("TransfertFrom1To2", 50, accountsHashtable.get(2L).getAccountNumber(), false,
				flowsDate, accountsHashtable.get(1L).getAccountNumber()));
		return generatedFlows;
	}

	private static void updateBalances(List<Flow> flows, Hashtable<Long, Account> accountsHashtable) {
		for (Flow flow : flows) {
			accountsHashtable.get(flow.getTargetAccountNumber()).setBalance(flow);
			if (flow instanceof Transfert) {
				accountsHashtable.get(((Transfert) flow).getIssuierAccountNumber()).setBalance(flow);
			}
		}
		Predicate<Account> hasNegativeBalance = account -> account.getBalance() < 0;
		Optional<Account> accountWithNegativeBalance = accountsHashtable.values().stream().filter(hasNegativeBalance)
				.findAny();
		accountWithNegativeBalance.ifPresent(account -> System.out
				.println("Compte avec un solde négatif trouvé : account n°" + account.getAccountNumber()));
	}

	private static List<Flow> fillFlowArrayWithJson() {
		List<Flow> flows = new ArrayList<Flow>();
		Path path = Paths.get("src/flows.json");

		try {
			// Lire le fichier JSON en tant que chaîne de caractères
			String jsonContent = new String(Files.readAllBytes(path));

			// Initialiser un objet Gson
			Gson gson = new GsonBuilder().registerTypeAdapter(Flow.class, new FlowTypeAdapter()).create();

			Type flowListType = new TypeToken<List<Flow>>() {
			}.getType();
			flows = gson.fromJson(jsonContent, flowListType);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return flows;
	}

	private static List<Account> fillAccountArrayWithXml(List<Client> clients) {
		List<Account> accounts = new ArrayList<>();
		Path path = Paths.get("src/accounts.xml");

		try {
			// Créer un DocumentBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Charger le fichier XML
			Document document = builder.parse(Files.newInputStream(path));
			document.getDocumentElement().normalize();

			// Récupérer tous les éléments "account"
			NodeList accountNodes = document.getElementsByTagName("account");

			for (int i = 0; i < accountNodes.getLength(); i++) {
				Element accountElement = (Element) accountNodes.item(i);

				// Récupérer le type de compte
				String type = accountElement.getElementsByTagName("type").item(0).getTextContent();

				// Récupérer le label
				String label = accountElement.getElementsByTagName("label").item(0).getTextContent();

				// Récupérer l'id du client et vérifier qu'il correspond à un client existant
				Element clientElement = (Element) accountElement.getElementsByTagName("client").item(0);
				Long clientId = Long
						.valueOf(clientElement.getElementsByTagName("clientNumber").item(0).getTextContent());
				Client client = clients.stream().filter(c -> Objects.equals(c.getClientNumber(), clientId)).findAny()
						.orElseThrow(() -> new IllegalArgumentException("No such existing client: " + clientId));

				// Créer une instance de l'Account selon son type
				Account account = null;
				switch (type) {
				case "CurrentAccount":
					account = new CurrentAccount(label, client);
					break;
				case "SavingsAccount":
					account = new SavingsAccount(label, client);
					break;
				default:
					throw new IllegalArgumentException("Unknown account type: " + type);
				}

				// Ajouter l'account à la liste
				accounts.add(account);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return accounts;
	}

}
