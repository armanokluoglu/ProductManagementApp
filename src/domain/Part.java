package domain;

import org.json.JSONObject;
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

	public JSONObject getJson(){
		JSONObject json = new JSONObject();
		json.put("number",getNumber());
		json.put("name",getName());
		json.put("status",getStatus());
		json.put("cost",getCost());
		return json;
	}

}