package gcubeit.com.enerwireproduccionv12.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gcubeit.com.enerwireproduccionv12.R;

public class SearchableSpinner extends AppCompatSpinner implements View.OnTouchListener, SearchableListDialog.SearchableItem {
    String selectedItem;
    // this string above will store the value of selected item.

    public static final int NO_ITEM_SELECTED = -1;
    private Context _context;
    private List _items;
    private SearchableListDialog _searchableListDialog;

    private boolean _isDirty;
    private ArrayAdapter _arrayAdapter;
    private String _strHintText;
    private boolean _isFromInit;

    public SearchableSpinner(Context context) {
        super(context);
        this._context = context;
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        final int N = a.getIndexCount();
        for(int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if(attr == R.styleable.SearchableSpinner_hintText) {
                _strHintText = a.getString(attr);
            }
        }

        a.recycle();
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init();
    }

    private void init() {
        _items = new ArrayList();
        _searchableListDialog = SearchableListDialog.newInstance(_items);
        _searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);

        _arrayAdapter = (ArrayAdapter) getAdapter();
        if(!TextUtils.isEmpty(_strHintText)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(_context, R.layout.spinner_item_base, new String[]{_strHintText});
            _isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(null != _arrayAdapter) {
                // Refresh content #6
                // Change Start
                // Description: The items where only set initially, not reloading the data in the
                // spinner every time it is loaded with items in the adapter.
                _items.clear();
                for(int i = 0; i < _arrayAdapter.getCount(); i++) {
                    _items.add(_arrayAdapter.getItem(i));
                }
                // Change end.

                _searchableListDialog.show(scanForActivity(_context).getFragmentManager(), "TAG");
            }
        }

        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if(!_isFromInit) {
            _arrayAdapter = (ArrayAdapter) adapter;
            if(!TextUtils.isEmpty(_strHintText) && !_isDirty) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(_context, R.layout.spinner_item_base, new String[]{_strHintText});
                super.setAdapter(arrayAdapter);
            } else {
                super.setAdapter(adapter);
            }
        } else {
            _isFromInit = false;
            super.setAdapter(adapter);
        }
    }

    // The method jus below is executed when an item in the searchlist is tapped. This is where we store the value int string called selectedItem.
    @Override
    public void onSearchableItemClicked(Object item, int position) {
        setSelection(_items.indexOf(item));

        if(!_isDirty) {
            _isDirty = true;
            setAdapter(_arrayAdapter);
            setSelection(_items.indexOf(item));
        }

        selectedItem = getItemAtPosition(position).toString();
    }

    private Activity scanForActivity(Context cont) {
        if(cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if(!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if(!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }

    public void setSelectionM(int position) {
        if(position != NO_ITEM_SELECTED) {
            if(!_isDirty) {
                _isDirty = true;
                setAdapter(_arrayAdapter);
                super.setSelection(position);
            }
        }
    }

    public void setSelectionP(int position, String key) {
        if(position != NO_ITEM_SELECTED) {
            if(!_isDirty) {
                _isDirty = true;
                setAdapter(_arrayAdapter);
                int index = 0;
                for (int i = 0; i <= getAdapter().getCount() + 1; i++) {
                    if(key.equals(Objects.requireNonNull(_arrayAdapter.getItem(i)).toString())){
                    //if(key == _arrayAdapter.getItem(i).toString()){
                        index = i;
                        //Toast.makeText(_context, Integer.toString(getAdapter().getCount()), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                //Toast.makeText(_context, _arrayAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                super.setSelection(index);
            }
        }
    }
}
