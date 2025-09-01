package org.merra.entities;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.merra.entities.embedded.InvoiceSettings;
import org.merra.entities.embedded.LineItemSettings;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is a configuration settings for organization object.
 * Setting default functionalities or values happen here.
 */
@Entity
@Table(name = "organization_setting", schema = "merra_schema")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationSettings {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "setting_id", nullable = false, unique = true)
	private UUID settingId;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", unique = true, referencedColumnName = "id")
	private Organization organization;
	
	// Default settings for invoices
	@Embedded
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "invoice_settings")
	private InvoiceSettings invoiceSettings;
	
	// Default settings for line items
	@Embedded
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "line_item_settings")
	private LineItemSettings lineItemSettings;
}

