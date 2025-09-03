package org.merra.entities.embedded;

import java.util.List;

import org.merra.entities.LineItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LineItemByAccountCode {
	private String accountCode;
	private int total;
	private List<LineItem> lineItems;
}
