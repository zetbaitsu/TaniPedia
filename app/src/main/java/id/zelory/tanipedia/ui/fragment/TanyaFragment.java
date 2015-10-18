/*
 * Copyright (c) 2015 Zetra.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package id.zelory.tanipedia.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.benih.util.BenihPreferenceUtils;
import id.zelory.tanipedia.R;
import id.zelory.tanipedia.controller.SoalController;
import id.zelory.tanipedia.controller.util.MyRecyclerScroll;
import id.zelory.tanipedia.data.model.Soal;
import id.zelory.tanipedia.ui.BookmarkActivity;
import id.zelory.tanipedia.ui.JawabActivity;
import id.zelory.tanipedia.ui.adapter.SoalAdapter;
import timber.log.Timber;

/**
 * Created on : October 15, 2015
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 */
public class TanyaFragment extends BaseFragment<SoalController, SoalAdapter> implements
        SoalController.Presenter
{
    @Bind(R.id.tanya_gambar) ImageView tanyaGambar;
    @Bind(R.id.myfab_main) FrameLayout frameFab;
    @Bind(R.id.myfab_main_btn) ImageButton fabAddPertanyaan;
    private MaterialDialog dialog;
    private int fabMargin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getFragmentView()
    {
        return R.layout.fragment_tanya;
    }

    @Override
    protected void onViewReady(Bundle bundle)
    {
        super.onViewReady(bundle);
        tanyaGambar.setVisibility(View.GONE);
        collapsingToolbar.setTitle("Tanya Tani");
        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);
        frameFab.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            ButterKnife.findById(getActivity(), R.id.myfab_shadow).setVisibility(View.GONE);
            fabAddPertanyaan.setBackground(getActivity().getDrawable(R.drawable.ripple_accent));
        }
        setUpLoadingDialog();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.tanya_tani, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.bookmark:
                Intent intent = new Intent(getActivity(), BookmarkActivity.class);
                intent.putExtra(BookmarkActivity.KEY_TYPE, BookmarkActivity.TYPE_SOAL);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void setUpLoadingDialog()
    {
        dialog = new MaterialDialog.Builder(getActivity())
                .title("TaniPedia")
                .content("Mengirim pertanyaan anda...")
                .progress(true, 0)
                .build();
    }

    @Override
    protected void onFabClick(View view)
    {
        controller.loadListSoal();
    }

    @OnClick(R.id.myfab_main_btn)
    void addPertanyaan()
    {
        new MaterialDialog.Builder(getActivity())
                .title("TaniPedia")
                .content("Kirim Pertanyaan")
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                .input("Ketik pertanyaan anda disini!", null, false, (dialog, input) -> {
                    controller.sendSoal(BenihPreferenceUtils.getString(getActivity(), "email"), input.toString());
                })
                .positiveColorRes(R.color.primary_dark)
                .positiveText("Kirim")
                .cancelListener(dialog -> {})
                .negativeColorRes(R.color.primary_dark)
                .negativeText("Batal")
                .show();
    }

    @Override
    protected void setUpRecyclerView()
    {
        super.setUpRecyclerView();
        recyclerView.addOnScrollListener(new MyRecyclerScroll()
        {
            @Override
            public void show()
            {
                frameFab.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator(2))
                        .start();
            }

            @Override
            public void hide()
            {
                frameFab.animate()
                        .translationY(frameFab.getHeight() + fabMargin)
                        .setInterpolator(new AccelerateInterpolator(2))
                        .start();
            }
        });
    }

    @Override
    protected void onItemClick(View view, int position)
    {
        Intent intent = new Intent(getActivity(), JawabActivity.class);
        intent.putExtra("soal", adapter.getData().get(position));
        startActivity(intent);
    }

    @Override
    protected SoalAdapter createAdapter()
    {
        return new SoalAdapter(getActivity());
    }

    @Override
    protected void setUpController(Bundle savedInstanceState)
    {
        if (controller == null)
        {
            controller = new SoalController(this);
        }

        if (savedInstanceState != null)
        {
            Timber.d("bundle is there");
            controller.loadState(savedInstanceState);
        } else if (getArguments() != null)
        {
            Timber.d("getArguments");
            controller.loadState(getArguments());
        } else
        {
            controller.loadListSoal();
        }
    }

    @Override
    public void showLoadingDialog()
    {
        dialog.show();
    }

    @Override
    public void dismissLoading()
    {
        super.dismissLoading();
        if (dialog != null)
        {
            dialog.dismiss();
        }
    }

    @Override
    public void showListSoal(List<Soal> listSoal)
    {
        adapter.clear();
        adapter.add(listSoal);
        recyclerView.startAnimation(animation);
        frameFab.startAnimation(animation);
        frameFab.setVisibility(View.VISIBLE);
        tanyaGambar.setVisibility(View.VISIBLE);
        tanyaGambar.startAnimation(animation);
    }
}
