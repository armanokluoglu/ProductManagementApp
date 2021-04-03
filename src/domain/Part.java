package domain;

import utilities.CatalogueEntry;
import utilities.StatusState;

public class Part extends Product {

	private CatalogueEntry entry;
	
	public Part(CatalogueEntry entry) {
		super(entry.getName(), entry.getNumber(), entry.getCost());
		setEntry(entry);
	}
	
	public CatalogueEntry getEntry() {
		return entry;
	}

	public void setEntry(CatalogueEntry entry) {
		this.entry = entry;
	}
	
	@Override
	public StatusState onChildStatusChange() {
		return null;
	}

}