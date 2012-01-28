package magic.ui;

import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicManaCost;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CardTableModel implements TableModel {	
	/**
	 * List of event listeners. These listeners wait for something to happen
	 * with the table so that they can react. This is a must!
	 */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	/**
	 * Adds a listener to the list that is notified each time a change to the
	 * model occurs.
	 * 
	 * @param l
	 *			the TableModelListener
	 */
	@Override
	public void addTableModelListener(TableModelListener l)
	{
		if (listeners.contains(l))
			return;
		listeners.add(l);
	}
	
	/**
	 * Removes a listener from the list that is notified each time a change to
	 * the model occurs.
	 * 
	 * @param l
	 *			the TableModelListener
	 */
	@Override
	public void removeTableModelListener(TableModelListener l)
	{
		listeners.remove(l);
	}
	
	public static final String[] COLUMN_NAMES = new String[] {	"#", // 0
																"Name", // 1
																"CC", // 2
																"P", // 3
																"T", // 4
																"Type", // 5
																"Subtype", // 6
																"Rarity", // 7
																"Text"}; // 8
	public static final int[] COLUMN_MIN_WIDTHS = new int[] {	40, // 0 #
																180, // 1 name
																140, // 2 cc
																30, // 3 P
																30, // 4 T
																140, // 5 type
																140, // 6 subtype 
																90, // 7 rarity
																2000}; // 8 text
	public static final int COST_COLUMN_INDEX = 2;
	
	private final boolean isDeck;
	
	private boolean[] isDesc = new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false};
	private MagicCondensedDeck cardDefinitions;
	private Comparator<MagicCondensedCardDefinition> comp;
	
	public CardTableModel(final List<MagicCardDefinition> cardDefs, final boolean isDeck) {
		this.comp = MagicCondensedCardDefinition.NAME_COMPARATOR_DESC;
		this.isDeck = isDeck;
		
		setCards(cardDefs);
	}
	
	public MagicCardDefinition getCardDef(int row) {
		if (row < 0 || row >= cardDefinitions.size()) {
			return null;
		}
		return cardDefinitions.get(row).getCard();
	}
	
	public int findCardIndex(MagicCardDefinition card) {
		for (int i = 0; i < cardDefinitions.size(); i++) {
			if(MagicCardDefinition.NAME_COMPARATOR_ASC.compare(cardDefinitions.get(i).getCard(), card) == 0) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void setCards(List<MagicCardDefinition> defs) {
		this.cardDefinitions = new MagicCondensedDeck(defs);
		
		// re-sort if necessary
		if(comp != null) {
			Collections.sort(cardDefinitions, comp);
		}
	}
	
	public void sort(int column) {
		Comparator<MagicCondensedCardDefinition> oldComp = comp;
		comp = null;
		
		switch(column) {
			case 0:		comp = (isDesc[column]) ? MagicCondensedCardDefinition.NUM_COPIES_COMPARATOR_ASC : MagicCondensedCardDefinition.NUM_COPIES_COMPARATOR_DESC;
						break;
			case 1:		comp = (isDesc[column]) ? MagicCondensedCardDefinition.NAME_COMPARATOR_ASC : MagicCondensedCardDefinition.NAME_COMPARATOR_DESC;
						break;
			case 2:		comp = (isDesc[column]) ? MagicCondensedCardDefinition.CONVERTED_COMPARATOR_ASC : MagicCondensedCardDefinition.CONVERTED_COMPARATOR_DESC;
						break;
			case 3:		comp = (isDesc[column]) ? MagicCondensedCardDefinition.POWER_COMPARATOR_ASC : MagicCondensedCardDefinition.POWER_COMPARATOR_DESC;
						break;
			case 4:		comp = (isDesc[column]) ? MagicCondensedCardDefinition.TOUGHNESS_COMPARATOR_ASC : MagicCondensedCardDefinition.TOUGHNESS_COMPARATOR_DESC;
						break;
			case 5:		comp = (isDesc[column]) ? MagicCondensedCardDefinition.TYPE_COMPARATOR_ASC : MagicCondensedCardDefinition.TYPE_COMPARATOR_DESC;
						break;
			case 7:		comp = (isDesc[column]) ? MagicCondensedCardDefinition.RARITY_COMPARATOR_ASC : MagicCondensedCardDefinition.RARITY_COMPARATOR_DESC;
						break;
		}
		
		if (comp != null) {
			// new sort
			Collections.sort(cardDefinitions, comp);
			isDesc[column] = !isDesc[column];			
		} else {
			// didn't select valid new sort -> reset to old
			comp = oldComp;
		}
	}
	
	/**
	 * Returns the most specific superclass for all the cell values in the
	 * column. This is used by the JTable to set up a default renderer and
	 * editor for the column.
	 * 
	 * @param columnIndex
	 *			the index of the column
	 * @return the common ancestor class of the object values in the model.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch (columnIndex) {
			case 1:		return MagicManaCost.class;
		}
		return String.class;
	}
	
	/**
	 * Returns the number of columns in the model. A JTable uses this method to
	 * determine how many columns it should create and display by default.
	 * 
	 * @return the number of columns in the model
	 * @see #getRowCount
	 */
	@Override
	public int getColumnCount()
	{
		return COLUMN_NAMES.length;
	}
	
	/**
	 * Returns the name of the column at columnIndex. This is used to initialize
	 * the table's column header name. Note: this name does not need to be
	 * unique; two columns in a table can have the same name.
	 * 
	 * @param columnIndex
	 *			the index of the column
	 * @return the name of the column
	 */
	@Override
	public String getColumnName(int columnIndex)
	{
		return COLUMN_NAMES[columnIndex];
	}
	
	/**
	 * Returns the number of rows in the model. A JTable uses this method to
	 * determine how many rows it should display. This method should be quick,
	 * as it is called frequently during rendering.
	 * 
	 * @return the number of rows in the model
	 * @see #getColumnCount
	 */
	@Override
	public int getRowCount()
	{
		return cardDefinitions.size();
	}
	
	/**
	 * Returns the value for the cell at columnIndex and rowIndex.
	 * 
	 * @param rowIndex
	 *			the row whose value is to be queried
	 * @param columnIndex
	 *			the column whose value is to be queried
	 * @return the value Object at the specified cell
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		MagicCardDefinition card = cardDefinitions.get(rowIndex).getCard();
		
		switch(columnIndex) {
			case 0:		if(isDeck) {
							return Integer.toString(cardDefinitions.get(rowIndex).getNumCopies());
						} else {
							return "";
						}
			case 1:		return card.getFullName();
			case 2:		return card.getCost();
			case 3:		if(card.isCreature()) {
							return card.getCardPower();
						} else {
							return "";
						}
			case 4:		if(card.isCreature()) {
							return card.getCardToughness();
						} else {
							return "";
						}
			case 5:		return card.getLongTypeString();
			case 6:		return card.getSubTypeString();
			case 7:		return card.getRarityString();
			case 8:		return card.getText();
		}
		
		return "";
	}
	
	/**
	 * Returns true if the cell at rowIndex and columnIndex is editable.
	 * Otherwise, setValueAt on the cell will not change the value of that cell.
	 * 
	 * @param rowIndex
	 *			the row whose value to be queried
	 * @param columnIndex
	 *			the column whose value to be queried
	 * @return true if the cell is editable
	 * @see #setValueAt
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false; // don't allow editing
	}
	
	/**
	 * Sets the value in the cell at columnIndex and rowIndex to value.
	 * 
	 * @param value
	 *			the new value
	 * @param rowIndex
	 *			the row whose value is to be changed
	 * @param columnIndex
	 *			the column whose value is to be changed
	 * @see #getValueAt
	 * @see #isCellEditable
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
	}

}

