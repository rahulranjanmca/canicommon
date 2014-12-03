package com.canigenus.common.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

@ManagedBean(eager = true)
@ApplicationScoped
public class GenericConverter extends AbstractConverter {

	private static final long serialVersionUID = 1068561057601213696L;

	
	private class SelectItemComparator implements Comparator<SelectItem> {

		public int compare(SelectItem firstObj, SelectItem secondObj) {
			if (!firstObj.getClass().equals(secondObj.getClass())) {
				System.out.println("Compared objects have different classes");
				return 0;
			}
			if (!(firstObj instanceof SelectItem)) {
				return 0;
			} else {
				SelectItem firstItem = (SelectItem) firstObj;
				SelectItem secondItem = (SelectItem) secondObj;
				return firstItem.getLabel().compareTo(secondItem.getLabel());
			}
		}

	}

	public void setRbKeyPrefix() {
	}

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean isSorted) {
		sorted = isSorted;
	}

	public List<String> getListElements() {
		return listElements;
	}

	public void setListElements(List<String> listElements) {
		this.listElements = listElements;
	}

	protected List<SelectItem> getSelectItemList(String firstItemValue,
			String firstItemLabel) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle rb = context.getApplication().getResourceBundle(context,
				"bundle");
		ArrayList<SelectItem> selectItemList = new ArrayList<SelectItem>();
		if (firstItemValue != null || firstItemLabel != null)
			selectItemList.add(new SelectItem(firstItemValue, rb
					.getString(firstItemLabel)));
		Iterator<String> i$ = getListElements().iterator();
		do {
			if (!i$.hasNext())
				break;
			String element = (String) i$.next();
			try {
				SelectItem selectItem = new SelectItem(element,
						rb.getString((new StringBuilder()).append(rbKeyPrefix)
								.append(element.trim()).toString()));
				selectItemList.add(selectItem);
				if (sorted)
					Collections
							.sort(selectItemList, new SelectItemComparator());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} while (true);
		return selectItemList;
	}

	protected List<SelectItem> getSelectItemListWithOptGroup(
			String firstItemValue, String firstItemLabel) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle rb = context.getApplication().getResourceBundle(context,
				"bundle");
		ArrayList<SelectItem> selectItemList = new ArrayList<SelectItem>();

		if (firstItemValue != null || firstItemLabel != null)
			selectItemList.add(new SelectItem(firstItemValue, rb
					.getString(firstItemLabel)));

		Set<String> optGroups = getMapForOptGroup().keySet();

		for (String optGroup : optGroups) {
			SelectItemGroup group1 = new SelectItemGroup(
					rb.getString((new StringBuilder()).append(rbKeyPrefix)
							.append(optGroup.trim()).toString()));
			List<SelectItem> list = new ArrayList<SelectItem>();
			for (String element : getMapForOptGroup().get(optGroup)) {

				SelectItem selectItem = new SelectItem(element,
						rb.getString((new StringBuilder()).append(rbKeyPrefix)
								.append(element.trim()).toString()));
				list.add(selectItem);
			}
			group1.setSelectItems(list
					.toArray(new SelectItem[getMapForOptGroup().get(optGroup)
							.size()]));
			selectItemList.add(group1);

		}

		return selectItemList;
	}

	protected List<SelectItem> getSelectItemListWithOptGroupFromRB(
			String firstItemValue, String firstItemLabel) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle rb = context.getApplication().getResourceBundle(context,
				"bundle");
		ArrayList<SelectItem> selectItemList = new ArrayList<SelectItem>();

		if (firstItemValue != null || firstItemLabel != null)
			selectItemList.add(new SelectItem(firstItemValue, rb
					.getString(firstItemLabel)));

		List<SelectItem> groupItems = new ArrayList<SelectItem>();
		SelectItemGroup group = null;
		Iterator<String> i$ = getListElements().iterator();
		do {
			if (!i$.hasNext())
				break;
			String element = (String) i$.next();
			try {

				if (element.endsWith(".group")) {
					groupItems.clear();
					group = new SelectItemGroup(
							rb.getString((new StringBuilder())
									.append(rbKeyPrefix).append(element.trim())
									.toString()));
					selectItemList.add(group);
				}

				else if (element.endsWith(".endgroup")) {
					groupItems.add(new SelectItem(element.replace(".endgroup",
							""), rb.getString((new StringBuilder())
							.append(rbKeyPrefix).append(element.trim())
							.toString())));
					group.setSelectItems(groupItems
							.toArray(new SelectItem[groupItems.size()]));
					System.out.println("Group Items:"+groupItems.size());
					group = null;
				} else {
					if (group == null) {
						SelectItem selectItem = new SelectItem(element,
								rb.getString((new StringBuilder())
										.append(rbKeyPrefix)
										.append(element.trim()).toString()));
						selectItemList.add(selectItem);
					} else {
						groupItems.add(new SelectItem(element, rb
								.getString((new StringBuilder())
										.append(rbKeyPrefix)
										.append(element.trim()).toString())));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} while (true);
		return selectItemList;
	}

	protected List<SelectItem> getSelectItemListWithOptGroupFromRB(
			String firstItemValue, String firstItemLabel, String parentKey) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle rb = context.getApplication().getResourceBundle(context,
				"bundle");
		ArrayList<SelectItem> selectItemList = new ArrayList<SelectItem>();

		if (firstItemValue != null || firstItemLabel != null)
			selectItemList.add(new SelectItem(firstItemValue, rb
					.getString(firstItemLabel)));
		if (parentKey == null || parentKey.equals("")) {
			return selectItemList;
		}

		List<SelectItem> groupItems = new ArrayList<SelectItem>();
		SelectItemGroup group = null;
		Iterator<String> i$ = getListElements().iterator();
		do {
			if (!i$.hasNext())
				break;
			String element = (String) i$.next();
			if (element.startsWith(parentKey + ".")) {
				try {

					if (element.contains(".group")) {
						group = new SelectItemGroup(
								rb.getString((new StringBuilder())
										.append(rbKeyPrefix)
										.append(element.trim()).toString()));
						selectItemList.add(group);
					}

					else if (element.contains(".endgroup")) {
						groupItems.add(new SelectItem(element.replace(
								".endgroup", "").replace(parentKey + ".", ""),
								rb.getString((new StringBuilder())
										.append(rbKeyPrefix)
										.append(element.trim()).toString())));
						group.setSelectItems(groupItems
								.toArray(new SelectItem[groupItems.size()]));
						System.out.println("Group Items:"+groupItems.size());
						group = null;
						groupItems.clear();
					} else {
						if (group == null) {
							SelectItem selectItem = new SelectItem(
									element.replace(parentKey + ".", ""),
									rb.getString((new StringBuilder()).append(
											element.trim()).toString()));
							selectItemList.add(selectItem);
						} else {
							groupItems
									.add(new SelectItem(element.replace(
											parentKey + ".", ""), rb
											.getString((new StringBuilder())
													.append(rbKeyPrefix)
													.append(element.trim())
													.toString())));
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} while (true);
		return selectItemList;
	}

	public Map<String, List<String>> getMapForOptGroup() {
		return mapForOptGroup;
	}

	public void setMapForOptGroup(Map<String, List<String>> mapForOptGroup) {
		this.mapForOptGroup = mapForOptGroup;
	}

	public List<SelectItem> getSelectItemListWithOptGroup() {
		return getSelectItemListWithOptGroup("", SELECT_ONE_RB_KEY);
	}

	public List<SelectItem> getSelectItemListWithOptGroupFromRB() {
		return getSelectItemListWithOptGroupFromRB("", SELECT_ONE_RB_KEY);
	}

	public List<SelectItem> getSelectItemListWithOptGroupFromRB(String parentKey) {
		return getSelectItemListWithOptGroupFromRB("", SELECT_ONE_RB_KEY,
				parentKey);
	}

	private List<String> listElements;

	private Map<String, List<String>> mapForOptGroup;
	private boolean sorted;
}
