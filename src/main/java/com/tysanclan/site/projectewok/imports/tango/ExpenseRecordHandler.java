/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.imports.tango;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;

/**
 * @author Jeroen Steenbeeke
 */
public class ExpenseRecordHandler implements RecordHandler {
	@SpringBean
	private FinanceService expenseService;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		expenseService = null;

	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "E";
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#handle(java.lang.String[],
	 *      com.tysanclan.site.projectewok.imports.tango.TangoImporterCallback)
	 */
	@Override
	public boolean handle(String[] data, TangoImporterCallback callback) {
		// E key name amount monthly from lastPaid to
		Long key = Long.parseLong(data[1]), fromStamp = Long.parseLong(data[5]), toStamp = Long
				.parseLong(data[7]);
		BigDecimal amount = new BigDecimal(data[3]);
		String name = data[2];
		boolean monthly = data[4].equals(VALUE_TRUE);

		Expense expense = expenseService.createExpense(name, amount,
				monthly ? ExpensePeriod.MONTHLY : ExpensePeriod.ANNUALLY,
				new Date(fromStamp));
		if (expense != null) {
			if (toStamp != 0L) {
				expenseService.setExpenseEnded(expense, new Date(toStamp));
			}

			callback.registerImportedObject(key, expense);
			return true;
		}

		return false;
	}

}
