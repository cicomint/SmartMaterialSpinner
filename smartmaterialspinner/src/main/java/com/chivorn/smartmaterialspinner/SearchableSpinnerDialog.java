package com.chivorn.smartmaterialspinner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.chivorn.smartmaterialspinner.util.SoftKeyboardUtil;

import java.io.Serializable;
import java.util.List;

public class SearchableSpinnerDialog extends DialogFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String LIST_ITEMS = "LIST_ITEMS";
    private static final String SAVE_INSTANCE_STATE_KEY = "SAVE_INSTANCE_STATE_KEY";
    private ArrayAdapter searchArrayAdapter;
    private ViewGroup searchHeaderView;
    private TextView tvSearchHeader;
    private SearchView searchView;
    private ListView searchListView;
    private TextView tvListItem;

    private boolean isEnableSearchHeader = true;
    private int headerBackgroundColor;
    private Drawable headerBackgroundDrawable;
    private int searchListItemColor;
    private int selectedSearchItemColor;
    private int selectedPosition = -1;

    private String searchHeaderText;
    private String searchHint;
    private int searchDialogGravity;

    private SearchableItem searchableItem;
    private OnSearchTextChanged onSearchTextChanged;
    private DialogInterface.OnClickListener dialogListener;

    public SearchableSpinnerDialog() {
    }

    public static SearchableSpinnerDialog newInstance(List items) {
        SearchableSpinnerDialog searchableSpinnerDialog = new SearchableSpinnerDialog();
        Bundle args = new Bundle();
        args.putSerializable(LIST_ITEMS, (Serializable) items);
        searchableSpinnerDialog.setArguments(args);
        return searchableSpinnerDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (savedInstanceState != null) {
            searchableItem = (SearchableItem) savedInstanceState.getSerializable(SAVE_INSTANCE_STATE_KEY);
        }
        View searchLayout = inflater.inflate(R.layout.smart_material_spinner_searchable_dialog_layout, null);
        initSearchDialog(searchLayout, savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(searchLayout);

        AlertDialog dialog = builder.create();
        searchDialogGravity = Gravity.TOP;
        setGravity(dialog);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                scrollToSelectedItem();
            }
        });
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initSearchDialog(View rootView, Bundle savedInstanceState) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchHeaderView = rootView.findViewById(R.id.search_header_layout);
        tvSearchHeader = rootView.findViewById(R.id.tv_search_header);
        searchView = rootView.findViewById(R.id.search_view);
        searchListView = rootView.findViewById(R.id.search_list_item);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        SoftKeyboardUtil.hideSoftKeyboard(getActivity());
        List items = (List) getArguments().getSerializable(LIST_ITEMS);
        searchArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.smart_material_spinner_search_list_item_layout, items) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View listView = super.getView(position, convertView, parent);
                tvListItem = listView.findViewById(R.id.tv_search_list_item);
                if (searchListItemColor != 0) {
                    tvListItem.setTextColor(searchListItemColor);
                }

                if (selectedSearchItemColor != 0 && position >= 0 && position == selectedPosition) {
                    tvListItem.setTextColor(selectedSearchItemColor);
                }
                return listView;
            }
        };
        searchListView.setAdapter(searchArrayAdapter);
        searchListView.setTextFilterEnabled(true);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchableItem.onSearchItemClickListener(searchArrayAdapter.getItem(position), position);
                getDialog().dismiss();
            }
        });
        initSearchHeader();
        initSearchBody();
    }

    private void initSearchHeader() {
        if (isEnableSearchHeader) {
            searchHeaderView.setVisibility(View.VISIBLE);
        } else {
            searchHeaderView.setVisibility(View.GONE);
        }

        if (searchHeaderText != null) {
            tvSearchHeader.setText(searchHeaderText);
        }

        if (headerBackgroundColor != 0) {
            searchHeaderView.setBackgroundColor(headerBackgroundColor);
        } else if (headerBackgroundDrawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                searchHeaderView.setBackground(headerBackgroundDrawable);
            }
        }
    }

    private void initSearchBody() {
        if (searchHint != null) {
            searchView.setQueryHint(searchHint);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_INSTANCE_STATE_KEY, searchableItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            ((ArrayAdapter) searchListView.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter) searchListView.getAdapter()).getFilter().filter(s);
        }
        if (onSearchTextChanged != null) {
            onSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchItemClickListener(T item, int position);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }

    public void setOnSearchDialogItemClickListener(SearchableItem searchableItem) {
        this.searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;
    }

    public void setEnableSearchHeader(boolean enableSearchHeader) {
        isEnableSearchHeader = enableSearchHeader;
    }

    public void setSearchHeaderText(String header) {
        searchHeaderText = header;
    }

    public void setSearchHeaderBackground(int color) {
        headerBackgroundColor = color;
        headerBackgroundDrawable = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setSearchHeaderBackground(Drawable drawable) {
        headerBackgroundDrawable = drawable;
        headerBackgroundColor = 0;
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
    }

    public void setSearchListItemColor(int searchListItemColor) {
        this.searchListItemColor = searchListItemColor;
    }

    public void setSelectedSearchItemColor(int selectedSearchItemColor) {
        this.selectedSearchItemColor = selectedSearchItemColor;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public void setSearchDialogGravity(int searchDialogGravity) {
        this.searchDialogGravity = searchDialogGravity;
        setGravity(getDialog());
    }

    private void setGravity(Dialog dialog) {
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(searchDialogGravity);
        }
    }

    private void scrollToSelectedItem() {
        if (selectedPosition >= 0 && searchListView.isSmoothScrollbarEnabled()) {
            searchListView.smoothScrollToPositionFromTop(selectedPosition, 0, 0);
        }
    }
}