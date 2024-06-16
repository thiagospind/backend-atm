package br.com.spindola.atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.spindola.atm.model.Client;
import br.com.spindola.atm.repository.BankAccountRepository;
import br.com.spindola.atm.repository.ClientRepository;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Act
        Client result = clientService.findById(clientId);

        // Assert
        assertEquals(client, result);
    }

    @Test
    public void testFindByIdNotFound() {
        // Arrange
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> clientService.findById(clientId));
    }

    @Test
    public void testCreate() {
        // Arrange
        Client client = new Client();
        client.setId(null);
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        Client result = clientService.create(client);

        // Assert
        assertEquals(client, result);
    }

    @Test
    public void testUpdate() {
        // Arrange
        Long clientId = 1L;
        Client existingClient = new Client();
        existingClient.setId(clientId);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));

        Client updatedClient = new Client();
        updatedClient.setId(clientId);
        when(clientRepository.save(updatedClient)).thenReturn(updatedClient);

        // Act
        Client result = clientService.update(updatedClient);

        // Assert
        assertEquals(updatedClient, result);
    }

    @Test
    public void testDelete() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Act
        clientService.delete(clientId);

        // Assert
        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    public void testDeleteWithBankAccounts() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        doThrow(new RuntimeException("Não é possível excluir um cliente que possui contas bancárias vinculadas"))
                .when(clientRepository).deleteById(clientId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> clientService.delete(clientId));
    }
}