package com.example.personalhealthmonitor.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.personalhealthmonitor.Database.Report;
import com.example.personalhealthmonitor.R;
import com.example.personalhealthmonitor.Utilities.Converters;
import com.example.personalhealthmonitor.Utilities.SnackbarUndo;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import static com.example.personalhealthmonitor.Utilities.Utility.*;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {

    //VARIABILI
    private final LayoutInflater layoutInflater;
    private final Context mContext;
    private List<Report> mReports;

    //COSTRUTTORE
    public ReportListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        if(mReports != null){
            Report report = mReports.get(position);
            holder.setData(report, position);
            holder.setListeners();
        }
    }

    @Override
    public int getItemCount() {
        if(mReports != null)
            return mReports.size();
        else return 0;
    }

    public void setReports(List<Report> reports) {
        mReports = reports;
        notifyDataSetChanged();
    }

    //VIEW HOLDER
    public class ReportViewHolder extends RecyclerView.ViewHolder{

        private int mPosition;
        private final ImageView IMGDelete;
        private final ImageView IMGEdit;

        //COSTRUTTORE
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            IMGDelete = itemView.findViewById(R.id.IMGDelete);
            IMGEdit = itemView.findViewById(R.id.IMGEdit);
        }

        //ASSEGNA I VALORI DEL REPORT ALLE ETICHETTE
        public void setData(Report report, int position) {
            TextView TXVGiorno = itemView.findViewById(R.id.TXVreport_title);
            TXVGiorno.setText("Report aggiunto il "+ Converters.DateToString(report.getData())+ " alle "+ report.getOra());
            TextView TXVBattito = itemView.findViewById(R.id.TXVbattito);
            if(report.getBattito() != 0){
                TXVBattito.setText(String.valueOf(report.getBattito()));
                itemView.findViewById(R.id.LLbattito).setVisibility(View.VISIBLE);
            }
            else{
                TXVBattito.setText("");
                itemView.findViewById(R.id.LLbattito).setVisibility(View.INVISIBLE);
            }
            TextView TXVPressioneSistolica = itemView.findViewById(R.id.TXVpressioneSistolica);
            TextView TXVPressioneDiastolica = itemView.findViewById(R.id.TXVpressioneDiastolica);
            if(report.getPressione_sistolica() != 0 || report.getPressione_diastolica() != 0){
                if(report.getPressione_sistolica() != 0) TXVPressioneSistolica.setText(String.valueOf(report.getPressione_sistolica()));
                if(report.getPressione_diastolica() != 0)TXVPressioneDiastolica.setText(String.valueOf(report.getPressione_diastolica()));
                itemView.findViewById(R.id.LLpressione).setVisibility(View.VISIBLE);
            }
            else{
                if(report.getPressione_sistolica() != 0) TXVPressioneSistolica.setText("");
                if(report.getPressione_diastolica() != 0)TXVPressioneDiastolica.setText("");
                itemView.findViewById(R.id.LLpressione).setVisibility(View.INVISIBLE);
            }
            TextView TXVTemperatura = itemView.findViewById(R.id.TXVtemperatura);
            if(report.getTemperatura() != 0){
                TXVTemperatura.setText(String.valueOf(report.getTemperatura()));
                itemView.findViewById(R.id.LLtemperatura).setVisibility(View.VISIBLE);
            }
            else {
                TXVTemperatura.setText("");
                itemView.findViewById(R.id.LLtemperatura).setVisibility(View.INVISIBLE);
            }
            TextView TXVGlicemiamax = itemView.findViewById(R.id.TXVglicemia_max);
            TextView TXVGlicemiamin = itemView.findViewById(R.id.TXVglicemia_min);
            if(report.getGlicemiamax() != 0 || report.getGlicemiamin() != 0){
                if(report.getGlicemiamax() != 0)TXVGlicemiamax.setText(String.valueOf(report.getGlicemiamax()));
                if(report.getGlicemiamin() != 0)TXVGlicemiamin.setText(String.valueOf(report.getGlicemiamin()));
                itemView.findViewById(R.id.LLglicemia).setVisibility(View.VISIBLE);
            }
            else {
                if(report.getGlicemiamax() != 0)TXVGlicemiamax.setText("");
                if(report.getGlicemiamin() != 0)TXVGlicemiamin.setText("");
                itemView.findViewById(R.id.LLglicemia).setVisibility(View.INVISIBLE);
            }
            TextView TXVNote = itemView.findViewById(R.id.TXVnote);
            if(!report.getNota().equals("")){
                TXVNote.setText("Note: "+report.getNota());
                itemView.findViewById(R.id.TXVnote).setVisibility(View.VISIBLE);
            }
            else {
                TXVNote.setText("");
                itemView.findViewById(R.id.TXVnote).setVisibility(View.INVISIBLE);
            }
            mPosition = position;
        }

        //ICONE DI DESTRA ELIMINA E MODIFICA
        public void setListeners() {
            //SE CLICCHI SU MODIFICA
            IMGEdit.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, NewReportActivity.class);
                int id = mReports.get(mPosition).getId();
                intent.putExtra("report_id", id);
                mContext.startActivity(intent);
            });

            //SE CLICCHI SU ELIMINA
            IMGDelete.setOnClickListener(v -> {
                Report report = mReports.get(mPosition);
                reportViewModel.deleteReport(report);

                SnackbarUndo SU = new SnackbarUndo();
                SU.reportRimosso(report);
                Snackbar snackbar = Snackbar.make(v, R.string.snackbar_label1, BaseTransientBottomBar.LENGTH_LONG);
                snackbar.setAction(R.string.snackbar_label2, SU);
                snackbar.show();
            });

        }
    }
}
