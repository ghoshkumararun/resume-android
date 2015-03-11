package ca.vijaysharma.resume.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;

import java.util.ArrayList;

import ca.vijaysharma.resume.R;
import ca.vijaysharma.resume.events.ShowDetailsEvent;
import ca.vijaysharma.resume.models.Experience;
import ca.vijaysharma.resume.models.Reference;
import ca.vijaysharma.resume.parcelable.DetailAction;
import ca.vijaysharma.resume.parcelable.DetailParcel;
import ca.vijaysharma.resume.parcelable.ReferenceItemSection;
import ca.vijaysharma.resume.parcelable.ReferenceSection;
import ca.vijaysharma.resume.parcelable.Section;
import ca.vijaysharma.resume.parcelable.TextSection;
import ca.vijaysharma.resume.utils.Action1;
import ca.vijaysharma.resume.utils.Drawables;
import ca.vijaysharma.resume.utils.Intents;
import ca.vijaysharma.resume.utils.Lists;
import de.greenrobot.event.EventBus;

public class ExperienceAdapter extends PagerAdapter {
    private final Context context;
    private final EventBus bus;
    private final Experience[] experiences;

    public ExperienceAdapter(
        Context context,
        EventBus bus,
        Experience[] experiences
    ) {
        this.context = context;
        this.bus = bus;
        this.experiences = experiences;
    }

    @Override
    public int getCount() {
        return 1 + experiences.length;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = null;

        if (position == 0) {
            view = new TextButtonBuilder<>(this.context, null)
                .setText("Experience")
                .setConnectorColor(this.context.getResources().getColor(R.color.purple))
                .setBackgroundDrawable(this.context.getDrawable(R.drawable.purple_circle_button))
                .setAddConnection(false)
                .build();
        } else {
            final Experience experience = experiences[position - 1];
            final Section company = TextSection.create("Company", Lists.newArrayList(experience.getSummary()));
            final Section work = TextSection.create("Experience", Lists.newArrayList(experience.getJobs()));

            ArrayList<ReferenceItemSection> items = new ArrayList<>();
            for (Reference reference : experience.getReferences()) {
                items.add(ReferenceItemSection.create(
                    reference.getName(),
                    reference.getName(),
                    reference.getAvatar())
                );
            }
            final Section references = ReferenceSection.create("References", items);

            view = new ImageButtonBuilder<>(this.context, new Object())
                .setConnectorColor(this.context.getResources().getColor(R.color.purple))
                .setBackgroundDrawable(Drawables.borderDrawable(this.context, R.color.purple))
                .setImage(experience.getLogo())
                .setAddConnection(true)
                .setListener(new Action1<Object>() {
                    @Override
                    public void call(Object item) {
                        DetailParcel parcel = DetailParcel.builder()
                            .detail1(experience.getName())
                            .detail2(experience.getPosition())
                            .detail3(timeSpent(experience))
                            .hero(experience.getLogo())
                            .primaryColor(experience.getPrimaryColor())
                            .secondaryColor(experience.getSecondaryColor())
                            .tertiaryColor(experience.getTertiaryColor())
                            .action1(DetailAction.builder()
                                .action(R.drawable.ic_public_white_24dp)
                                .intent(Intents.createUrlIntent(experience.getWebsite()))
                                .build())
                            .action2(DetailAction.builder()
                                .action(R.drawable.ic_place_white_24dp)
                                .intent(Intents.createEmailIntent(experience.getLocation()))
                                .build())
                            .sections(Lists.newArrayList(
                                company, work, references
                            ))
                            .build();

                        bus.post(new ShowDetailsEvent(parcel));
                    }
                })
                .build();
        }

        collection.addView(view);

        return view;
    }

    private String timeSpent(Experience experience) {
        final Years years = Years.yearsBetween(experience.getStart(), experience.getEnd());
        if (years.getYears() != 0) {
            return years.getYears() + " years";
        }

        final Months months = Months.monthsBetween(experience.getStart(), experience.getEnd());
        if (months.getMonths() != 0) {
            return months.getMonths() + " months";
        }

        final Days days = Days.daysBetween(experience.getStart(), experience.getEnd());
        return days.getDays() + " days";
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}