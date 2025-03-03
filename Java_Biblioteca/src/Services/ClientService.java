package Services;

import java.util.List;

import javax.persistence.EntityManager;

import DAO.ClientDAO;
import Entities.Client;

import Utils.Database;
import Utils.Input;

public class ClientService {
	private final Input input;
	private final ClientDAO clientDAO;
	private EntityManager manager;
	
	public ClientService(ClientDAO clientDAO, Input input) {
		this.clientDAO = clientDAO;
		this.input = input;
	}





	/*
	*
	* 		CRUD Methods
	*
	*/
	public void list() {
		
		try {

			manager = Database.openConnection();

			System.out.println("\n--- Lista de Clientes ---");

			for (Client client : clientDAO.list()) {

				System.out.println(client);

			}

		} finally {
			
			Database.closeConnection(manager);

		}
	}

	public void create() {

		try {
			manager = Database.openConnection();
			manager.getTransaction().begin();

			System.out.println("\n--- Cadastro de Cliente ---");

			String name = input.stringInput("Name: ");


			Client client = new Client(name);

			clientDAO.save(client, manager);
			manager.getTransaction().commit();
			System.out.println("Cliente cadastrado com sucesso!\n");

		}


		catch(Exception e) {

			System.out.println("Erro: " + e.getMessage());

			if (manager.getTransaction().isActive()) {

				manager.getTransaction().rollback();

			}

		}

		finally {

			System.out.println("\nClientes cadastrados com sucesso!\n");
			Database.closeConnection(manager);

		}
	}

	public void delete() {

		try {

			manager = Database.openConnection();
			manager.getTransaction().begin();

			System.out.println("\n--- Realizar Exclusão - Clientes ---");

			List<Client> clientList = clientDAO.list();

			if (clientList.isEmpty()) {

				System.out.println("Não há clientes cadastrados.");

			} else {

				for( int i=0;i < clientList.size(); i++) {

					System.out.println((i + 1) +". " + clientList.get(i).getName() + " ID: " + clientList.get(i).getId());

				}

				int option;

				option = input.integerInput("Qual opcao deseja deletar: ");
				if (option < 1 || option > clientList.size()) {

					System.out.println("Opção fora de intervalo, tente novamente.");

				}
				// Selected <Client> from For-Loop
				Client selectedClient = clientList.get(option -1);

				String confirmedClient = input.stringInput("Tem certeza de qe deseja excluir o cliente " + selectedClient.getName() + ", CPF: " + selectedClient.getId() + "(s/n): ");

				// Based on confirmedClient, we get into that if-else block right above.
				if (confirmedClient.equalsIgnoreCase("s")) {

					clientDAO.delete(selectedClient, manager);
					manager.getTransaction().commit();
					System.out.println("Cliente excluído com sucesso.");

				} else {

					System.out.println("Operação cancelada.");

				}
			}
		}
		catch( Exception e) {

			System.out.println("Erro: " + e.getMessage());
			manager.getTransaction().rollback();

		} finally {

			Database.closeConnection(manager);

		}
	}

	public void update() {

		manager = Database.openConnection();
		manager.getTransaction().begin();

		System.out.println("\n --- Editar Cliente ---");
		List<Client> clientList = clientDAO.list();

		try {

			if(clientList.isEmpty()) {

				System.out.println("Nao existe cliente cadastrado.");

			} else {

				for (int c = 0; c < clientList.size(); c++) {

					System.out.println(clientList.get(c).getName() + " ID: " + clientList.get(c).getId());

				}

				int option;

				option = input.integerInput("Selecione um Cliente: ");

				if (option < 1 || option > clientList.size()) {

					System.out.println("Opcao fora do intervalo, tente novamente.");
					return;

				}

				Client selectedClient = clientList.get(option -1);
				String setNewClientName = input.stringInput("Deseja alterar o nome do Cliente em questao? Nome Atual" + selectedClient.getName());
				if (setNewClientName.equalsIgnoreCase("n")) {

					System.out.println("Operacao cancelada, nome sera mantido.");

				} else {
					selectedClient.setName(setNewClientName);
					System.out.println("Cliente alterado com sucesso.");

					clientDAO.update(selectedClient, manager);
				}

			}

		}

		catch (Exception e) {

			System.out.println("Erro: " + e.getMessage());

			manager.getTransaction().rollback();

		}

		finally {

			manager.getTransaction().commit();

			Database.closeConnection(manager);

		}

	}


	/// @TODO: Pelo amor de Deus, so falta isso pra gente ir pros demais services terminar essa parte do projeto.
	public void search() {
		manager = Database.openConnection();
		System.out.println("\n--- Busca Clientes ---");
	}
}
