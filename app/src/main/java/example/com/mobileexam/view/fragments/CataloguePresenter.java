package example.com.mobileexam.view.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.com.mobileexam.BuildConfig;
import example.com.mobileexam.helper.VolleyRequestHelper;
import example.com.mobileexam.model.dto.CatalogueResponseDTO;
import example.com.mobileexam.model.dto.CatalogueResult;

import static com.google.common.base.Preconditions.checkNotNull;
import static example.com.mobileexam.utils.Parser.parseGeneric;

/**
 * Created by kestrella on 2/9/18.
 */

public class CataloguePresenter implements CatalogueContract.Presenter {

  private final CatalogueContract.View mCatalogueView;
  private List<CatalogueResult> mCatalogueResults = new ArrayList<>();
  List<CatalogueResult> localList = new ArrayList<>();
  private final Context context;
  private VolleyRequestHelper volleyRequestHelper;


  public CataloguePresenter(Context context, @NonNull CatalogueContract.View catalogueView) {
    mCatalogueView = checkNotNull(catalogueView, "catalogueView cannot be null!");
    mCatalogueView.setPresenter(this);
    volleyRequestHelper = new VolleyRequestHelper(context, requestCompletedListener);
    this.context = context;
  }


  @Override
  public void start() {
    mCatalogueResults.clear();
    loadCarList(false, 1);
  }

  @Override
  public void loadCarList(boolean willForceUpdate, int page) {
    String url = BuildConfig.API_URL + "/api/cars/page:" + page + "/maxitems:10/";

    makeLog(url);
    mCatalogueView.showLoader();
    volleyRequestHelper.makeStringGETRequest(url, "cars");
  }

  @Override
  public void setSorting(String sorting) {
    //todo
  }

  @Override
  public String getSorting() {
    return null; // todo
  }

  @Override
  public void openCarDetails(@NonNull CatalogueResult catalogueResult) {

  }

  /* The request completed listener */
  private VolleyRequestHelper.OnRequestCompletedListener requestCompletedListener = new VolleyRequestHelper.OnRequestCompletedListener() {
    @Override
    public void onRequestCompleted(String requestName, boolean status,
                                   String response, String errorMessage) {
      mCatalogueView.hideLoader();
      if (status) {
        CatalogueResponseDTO catalogueResponseDTO = parseGeneric(response, CatalogueResponseDTO.class);
        mCatalogueResults.addAll(catalogueResponseDTO.getMetadata().getResults());
        mCatalogueView.showData(mCatalogueResults);
      }

    }
  };

  private void makeLog(String message) {
    Log.w("CataloguePresenter", "*****---->>> " + message);
  }

}
