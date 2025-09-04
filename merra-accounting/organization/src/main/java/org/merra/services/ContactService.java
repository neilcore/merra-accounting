package org.merra.services;

import org.merra.dto.ContactResponse;
import org.merra.dto.CreateContactRequest;
import org.merra.entities.Contact;
import org.merra.entities.Organization;
import org.merra.exceptions.OrganizationExceptions;
import org.merra.repositories.ContactRepository;
import org.merra.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {
	private final OrganizationRepository organizationRepository;
	private final ContactRepository contactRepository;
	
	public ContactResponse createContact(@NotNull CreateContactRequest request) {
		Organization getOrganization = organizationRepository.findById(request.organizationId())
				.orElseThrow(() -> new EntityNotFoundException(OrganizationExceptions.NOT_FOUND_ORGANIZATION));
		String contactName = request.name();
		Contact obj = new Contact(contactName, getOrganization);
		Contact newContact = contactRepository.save(obj);
		
		return new ContactResponse(newContact.getId(), newContact.getName());
	}

}
