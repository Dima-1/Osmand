package net.osmand.plus.profiles;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.R;
import net.osmand.plus.profiles.ProfileMenuAdapter.ProfileViewHolder;


public class ProfileMenuAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

	private List<AppProfile> items;
	private ProfileListener listener = null;
	private final OsmandApplication app;


	public ProfileMenuAdapter(List<AppProfile> items, OsmandApplication app, ProfileListener listener) {
		this.items = items;
		this.listener = listener;
		this.app = app;
	}

	public List<AppProfile> getItems() {
		return items;
	}

	public void addItem(AppProfile profileItem) {
		items.add(profileItem);
		notifyDataSetChanged();
	}

	public void updateItemsList(List<AppProfile> newList) {
		items.clear();
		items.addAll(newList);
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.profile_list_item, parent, false);
		return new ProfileViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull final ProfileViewHolder holder, int position) {
		final AppProfile item = items.get(position);
		holder.title.setText(item.getTitle());
		holder.title.setTextColor(app.getResources().getColor(isNightMode(app) ? R.color.main_font_dark : R.color.main_font_light));
		holder.descr.setText(String.format("Type: %s", item.getNavType()));
		holder.icon.setImageDrawable(app.getUIUtilities().getIcon(item.getIconRes(), isNightMode(app) ? R.color.active_buttons_and_links_dark : R.color.active_buttons_and_links_light));
		holder.aSwitch.setChecked(item.isSelected());
		holder.aSwitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.changeProfileStatus(item, holder.aSwitch.isChecked());
			}
		});
		holder.profileOptions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.editProfile(item);
			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	private static boolean isNightMode(OsmandApplication ctx) {
		return !ctx.getSettings().isLightContent();
	}

	class ProfileViewHolder extends RecyclerView.ViewHolder {
		TextView title, descr;
		SwitchCompat aSwitch;
		ImageView icon, profileOptions;

		ProfileViewHolder(View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.profile_title);
			descr = itemView.findViewById(R.id.profile_descr);
			aSwitch = itemView.findViewById(R.id.profile_switch);
			icon = itemView.findViewById(R.id.profile_icon);
			profileOptions = itemView.findViewById(R.id.profile_settings);
		}
	}

	public interface ProfileListener {
		void changeProfileStatus(AppProfile item, boolean isSelected);
		void editProfile(AppProfile item);
	}
}

