package org.merra.repositories;

import java.util.UUID;

import org.merra.entities.AccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCategoryRepository extends JpaRepository<AccountCategory, UUID> {
	// Category IDs
	static final UUID assetId = UUID.fromString("2ab37296-d514-499d-a8bf-4dcd545a10ef");
	static final UUID liabilityId = UUID.fromString("7620dee8-e2eb-42e1-af02-4a06e8964fb6");
	static final UUID equityId = UUID.fromString("53a1d7fa-88d0-4481-a86b-b73a082bd4fa");
	static final UUID expenseId = UUID.fromString("d91fffe9-c227-4dd5-accb-bb6271f2f1e3");
	static final UUID revenueId = UUID.fromString("4b9d0f41-ecba-456e-856a-40b909ebd181");
}
