package org.merra.controller;

import java.util.UUID;

import org.merra.api.ApiResponse;
import org.merra.dto.CreateOrganizationRequest;
import org.merra.dto.OrganizationDetailsResponse;
import org.merra.dto.OrganizationElementResponse;
import org.merra.dto.OrganizationUserInvitationUpdateRequest;
import org.merra.services.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * x-organization-id header is required for
 * accessing existing organization entity.
 */
@RestController
@RequestMapping("api/v1/business/organization/")
@RequiredArgsConstructor
public class OrganizationController {
	private final OrganizationService organizationService;
	
	@Operation(summary = "create new organization")
	@PostMapping("create")
	public ResponseEntity<ApiResponse> createOrganization(@RequestBody @Valid CreateOrganizationRequest data) {
		ApiResponse response = new ApiResponse();
		response.setData(organizationService.createNewOrganizationObject(data));
		response.setMessage("Organization object found successfully.");
		response.setStatus(HttpStatus.OK);
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * TODO finish updateOrganization method controller for updating
	 * organization entity
	 * @param id
	 * @param data
	 * @return
	 */
	@PutMapping("update/{id}")
	public ResponseEntity<OrganizationDetailsResponse> updateOrganization(
			@RequestHeader("x-organization-id") UUID xOrganizationHeaderId,
			@PathVariable(name = "id") UUID id,
			@Valid @RequestBody CreateOrganizationRequest data
	) {
		return null;
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ApiResponse> getOrganization(@PathVariable(required = true) UUID id) {
		ApiResponse response = new ApiResponse();
		response.setData(organizationService.retrieveOrganizationById(id));
		response.setMessage("Organization object found successfully.");
		response.setStatus(HttpStatus.OK);
		
		return ResponseEntity.ok(response);
	}
	
	// Get Organization Elements
	@GetMapping("elements")
	public ResponseEntity<OrganizationElementResponse> getOrganizationElements() {
		return null;
	}
	
	/**
	 * Method controller for handling user organization invitation
	 * @param id
	 * @param data
	 * @return
	 */
	@PutMapping("{id}/invite/users/update")
	public ResponseEntity<?> updatedUserOrganizationInvitation(
			@PathVariable("id") UUID id,
			@Valid @RequestBody OrganizationUserInvitationUpdateRequest data
	) {
		return null;
	}
	
	/**
	 * TODO work on delete method controller for organization
	 * @param id
	 * @return
	 */
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteOrganizationAccount(@PathVariable("id") UUID id) {
		return null;
	}
}
