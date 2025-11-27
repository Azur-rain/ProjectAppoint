package com.example.myapplication;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class pt_Appoint_MainActivity extends AppCompatActivity {

    private static final int ITEMS_PER_PAGE = 6;

    private TextView tvMonth, tvPageInfo;
    private ImageButton btnPrevMonth, btnNextMonth, btnPrevPage, btnNextPage, btnBack;
    private RecyclerView rvAppointments;
    private Button btnBookAppointment;
    private AppointmentAdapter adapter;

    private Calendar currentCalendar;
    private SimpleDateFormat monthYearFormat;
    private SimpleDateFormat dayOfWeekFormat;
    private List<Appointment> allAppointments;
    private List<Appointment> currentPageAppointments;
    private int currentPage = 0;
    private int totalPages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pt_activity_main);

        initializeViews();
        setupCalendar();
        setupRecyclerView();
        setupClickListeners();
        loadAppointments();
    }

    private void initializeViews() {
        tvMonth = findViewById(R.id.tvMonth);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        btnPrevMonth = findViewById(R.id.btnPrevMonth);
        btnNextMonth = findViewById(R.id.btnNextMonth);
        btnPrevPage = findViewById(R.id.btnPrevPage);
        btnNextPage = findViewById(R.id.btnNextPage);
        btnBack = findViewById(R.id.btnBack);
        rvAppointments = findViewById(R.id.rvAppointments);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
    }

    private void setupCalendar() {
        currentCalendar = Calendar.getInstance();
        monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        dayOfWeekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        updateMonthDisplay();
    }

    private void setupRecyclerView() {
        allAppointments = new ArrayList<>();
        currentPageAppointments = new ArrayList<>();
        adapter = new AppointmentAdapter(currentPageAppointments, (position, appointment) -> {
            // Silent selection - no toast notification
        });

        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvAppointments.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            // Navigate back or close the activity
            finish();
        });

        btnPrevMonth.setOnClickListener(v -> {
            // Prevent going to past months
            Calendar today = Calendar.getInstance();
            Calendar tempCalendar = (Calendar) currentCalendar.clone();
            tempCalendar.add(Calendar.MONTH, -1);

            // Check if the previous month would be before current month
            if (tempCalendar.get(Calendar.YEAR) < today.get(Calendar.YEAR) ||
                    (tempCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                            tempCalendar.get(Calendar.MONTH) < today.get(Calendar.MONTH))) {
                Toast.makeText(this, "Cannot view past months. Please book appointments from today onwards.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            currentCalendar.add(Calendar.MONTH, -1);
            updateMonthDisplay();
            loadAppointments();
        });

        btnNextMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateMonthDisplay();
            loadAppointments();
        });

        btnPrevPage.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                updateCurrentPage();
            }
        });

        btnNextPage.setOnClickListener(v -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                updateCurrentPage();
            }
        });

        btnBookAppointment.setOnClickListener(v -> {
            Appointment selected = adapter.getSelectedAppointment();
            if (selected != null) {
                // Check if the selected date is in the past
                Calendar today = Calendar.getInstance();
                Calendar selectedCal = selected.getCalendar();

                // Reset time to compare only dates
                Calendar todayDate = (Calendar) today.clone();
                todayDate.set(Calendar.HOUR_OF_DAY, 0);
                todayDate.set(Calendar.MINUTE, 0);
                todayDate.set(Calendar.SECOND, 0);
                todayDate.set(Calendar.MILLISECOND, 0);

                Calendar selectedDate = (Calendar) selectedCal.clone();
                selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                selectedDate.set(Calendar.MINUTE, 0);
                selectedDate.set(Calendar.SECOND, 0);
                selectedDate.set(Calendar.MILLISECOND, 0);

                if (selectedDate.before(todayDate)) {
                    Toast.makeText(this, "Cannot book appointments for past dates. Please select a current or future date.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Show success notification
                String message = "✓ Appointment Booked Successfully!\n\n" +
                        "Date: " + selected.getDate() + "\n" +
                        "Time: " + selected.getTime() + "\n\n" +
                        "You will receive a confirmation shortly.";

                Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
                toast.show();

                // Reset selection after booking
                adapter.clearSelection();

            } else {
                Toast.makeText(this, "Please select an appointment slot first",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Bottom navigation click listeners

        /*findViewById(R.id.navHome).setOnClickListener(v ->
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show());

        findViewById(R.id.navAppointments).setOnClickListener(v ->
                Toast.makeText(this, "Appointments", Toast.LENGTH_SHORT).show());

        findViewById(R.id.navNotifications).setOnClickListener(v ->
                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show());

        findViewById(R.id.navProfile).setOnClickListener(v ->
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show());*/
    }

    private void updateMonthDisplay() {
        tvMonth.setText(monthYearFormat.format(currentCalendar.getTime()));
    }

    private void loadAppointments() {
        allAppointments.clear();

        // Get current date for comparison
        Calendar today = Calendar.getInstance();

        // Create a calendar for iterating through the month
        Calendar tempCalendar = (Calendar) currentCalendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);

        int maxDaysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Always start from day 1 of the selected month
        int startDay = 1;

        // But if it's the current month, start from today
        if (tempCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                tempCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
            startDay = today.get(Calendar.DAY_OF_MONTH);
        }

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String monthName = monthFormat.format(tempCalendar.getTime());

        // Iterate through ALL days from startDay to end of month
        for (int day = startDay; day <= maxDaysInMonth; day++) {
            tempCalendar.set(Calendar.DAY_OF_MONTH, day);

            int dayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);

            // Check if it's a weekday (Monday=2 to Friday=6)
            if (dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY) {
                String dayName = dayOfWeekFormat.format(tempCalendar.getTime());
                String dateString = monthName + " " + day + " (" + dayName + ")";

                Calendar appointmentCal = (Calendar) tempCalendar.clone();
                allAppointments.add(new Appointment(dateString, "9:00 A.M.",
                        appointmentCal, dayName));
            }
        }

        // Calculate total pages
        totalPages = (int) Math.ceil((double) allAppointments.size() / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;

        // Reset to first page
        currentPage = 0;
        updateCurrentPage();
    }

    @SuppressLint("SetTextI18n")
    private void updateCurrentPage() {
        currentPageAppointments.clear();

        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, allAppointments.size());

        for (int i = startIndex; i < endIndex; i++) {
            currentPageAppointments.add(allAppointments.get(i));
        }

        adapter.updateAppointments(currentPageAppointments);

        // Update page info
        tvPageInfo.setText("Page " + (currentPage + 1) + " of " + totalPages);

        // Enable/disable page navigation buttons
        btnPrevPage.setEnabled(currentPage > 0);
        btnNextPage.setEnabled(currentPage < totalPages - 1);

        // Visual feedback for disabled buttons
        btnPrevPage.setAlpha(currentPage > 0 ? 1.0f : 0.3f);
        btnNextPage.setAlpha(currentPage < totalPages - 1 ? 1.0f : 0.3f);
    }
}