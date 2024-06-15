package br.com.spindola.atm.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.spindola.atm.model.Client;
import br.com.spindola.atm.model.Client.CreateClient;
import br.com.spindola.atm.model.Client.UpdateClient;
import br.com.spindola.atm.service.ClientService;
import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/client")
@Validated
public class ClientController {
  
  @Autowired
  private ClientService clientService;

  @GetMapping("/{id}")
  public ResponseEntity<Client> findById(@PathVariable Long id) {
    Client client = this.clientService.findById(id);
    return ResponseEntity.ok().body(client);
  }

  @PostMapping
  @Validated(CreateClient.class)
  public ResponseEntity<Void> create(@Valid @RequestBody Client obj) {
    this.clientService.create(obj);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}").buildAndExpand(obj.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping
  @Validated(UpdateClient.class)
  public ResponseEntity<Void> update(@Valid @RequestBody Client obj, @PathVariable Long id) {
    obj.setId(id);
    this.clientService.update(obj);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    this.clientService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
