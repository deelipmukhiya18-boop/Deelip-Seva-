package com.example.data

import com.example.model.ToolGuideItem

object ToolsCatalog {

    fun getExtended500PlusTools(): List<ToolGuideItem> {
        val tools = ArrayList<ToolGuideItem>(550)

        // 1. Base Core Tools (50 items)
        val coreSections = listOf(
            Triple(1, "Communication & Social", listOf(
                Triple("send_whatsapp", "WhatsApp chat kholta hai aur message bhejta hai", "\"Rahul ko WhatsApp pe bolo hello\""),
                Triple("send_sms", "Direct SMS bhejta hai recipient ko", "\"Mummy ko SMS karo main pahuch gaya\""),
                Triple("open_email_inbox", "Gmail aur Outlook inbox kholta hai", "\"Mera inbox kholo\""),
                Triple("compose_email", "Email composer kholta hai To/Subject ke saath", "\"Boss ko urgent leave email likho\""),
                Triple("send_emergency_alert", "Emergency contact ko location ke sath SOS alert bhejta hai", "\"SOS, meri madad karo\""),
                Triple("send_telegram_msg", "Telegram chat ya channel par message post karta hai", "\"Telegram pe updates bhejo\""),
                Triple("send_signal_msg", "Signal encrypted messenger par message bhejta hai", "\"Signal par secure message send karo\""),
                Triple("open_instagram_dm", "Instagram direct messages inbox kholta hai", "\"Instagram DM check karo\""),
                Triple("post_twitter_tweet", "X (Twitter) par status tweet compose karta hai", "\"Twitter pe naya tweet post karo\""),
                Triple("send_discord_msg", "Discord server text channel par message bhejta hai", "\"Discord general chat mein message bhejo\""),
                Triple("slack_send_msg", "Work Slack channel mein quick update post karta hai", "\"Slack team ko update bhej do\""),
                Triple("open_messenger", "Facebook Messenger kholta hai", "\"Messenger chat kholo\""),
                Triple("linkedin_connection_msg", "LinkedIn inbox khol kar message compose karta hai", "\"LinkedIn par message bhejo\""),
                Triple("reddit_open_sub", "Reddit par desired subreddit kholta hai", "\"Reddit tech news kholo\""),
                Triple("snapchat_open_camera", "Snapchat camera screen seedha kholta hai", "\"Snapchat kholo\""),
                Triple("pinterest_search_board", "Pinterest par ideas aur pins search karta hai", "\"Pinterest par home decor dhundo\""),
                Triple("threads_post_update", "Threads app par naya thought share karta hai", "\"Threads par post karo\""),
                Triple("open_google_meet", "Google Meet video call create ya join karta hai", "\"Google Meet link banao\""),
                Triple("open_zoom_meeting", "Zoom meeting join ya start karta hai", "\"Zoom meeting kholo\""),
                Triple("open_ms_teams", "Microsoft Teams meetings aur chat kholta hai", "\"Teams open karo\"")
            )),
            Triple(2, "Calls & Telephony", listOf(
                Triple("call_contact", "Saved contact ya number par phone call lagata hai", "\"Papa ko phone lagao\""),
                Triple("lookup_contact", "Phonebook se contact details dhundta hai", "\"Rohit ka number kya hai\""),
                Triple("answer_call", "Aati hui phone call ko speaker par answer karta hai", "\"Call utha lo\""),
                Triple("end_call", "Chalti hui call ko reject ya disconnect karta hai", "\"Call kaat do\""),
                Triple("turn_on_speaker", "In-call speakerphone ko turant ON karta hai", "\"Speaker on karo\""),
                Triple("mute_microphone", "Call ke dauran microphone mute/unmute karta hai", "\"Mic mute karo\""),
                Triple("read_call_log", "Recent missed aur dialed calls padhta hai", "\"Recent calls list batao\""),
                Triple("speed_dial_1", "Speed dial number 1 par call dial karta hai", "\"Speed dial 1 lagao\""),
                Triple("toggle_call_recording", "Call audio recording prompt shuru karta hai", "\"Call record karo\""),
                Triple("call_voicemail", "Operator voicemail service dial karta hai", "\"Voicemail check karo\""),
                Triple("block_spam_number", "Suspected spam number ko block list mein daalta hai", "\"Is number ko block karo\""),
                Triple("conference_call_add", "Current call mein doosra contact add karta hai", "\"Call mein Priya ko add karo\""),
                Triple("send_call_decline_sms", "Call reject karke 'I will call back later' SMS bhejta hai", "\"Call reject karke SMS bhej do\""),
                Triple("check_sim_balance", "USSD balance check code dial karta hai", "\"Data balance check karo\""),
                Triple("switch_active_sim", "Primary calling SIM toggle karta hai", "\"Calling SIM 2 par switch karo\"")
            )),
            Triple(3, "Music & Media Playback", listOf(
                Triple("play_music", "Spotify ya YouTube Music par gaane search karke chalaata hai", "\"Arijit Singh ke gaane chalao\""),
                Triple("media_pause", "Chal rahe audio/video ko pause karta hai", "\"Gaana pause karo\""),
                Triple("media_resume", "Audio playback ko wapas resume karta hai", "\"Gaana chalu karo\""),
                Triple("media_next_track", "Playlist ka agla gaana play karta hai", "\"Next gaana lagao\""),
                Triple("media_prev_track", "Pichla gaana repeat ya piche karta hai", "\"Pichla gaana chalao\""),
                Triple("set_volume", "Volume level percentage mein adjust karta hai", "\"Volume 70% kar do\""),
                Triple("volume_up", "Media volume step up karta hai", "\"Awaaz badhao\""),
                Triple("volume_down", "Media volume step down karta hai", "\"Awaaz dheemi karo\""),
                Triple("shazam_identify_song", "Aas-paas baj rahe gaane ko pehchanta hai", "\"Ye kaun sa gaana baj raha hai\""),
                Triple("open_youtube_video", "YouTube video search karke open karta hai", "\"YouTube par coding tutorial kholo\""),
                Triple("open_spotify_playlist", "Liked songs playlist stream karta hai", "\"Mera Spotify favorite playlist chalao\""),
                Triple("open_netflix_show", "Netflix app launch karke search screen kholta hai", "\"Netflix open karo\""),
                Triple("open_prime_video", "Amazon Prime Video kholta hai", "\"Prime video kholo\""),
                Triple("open_hotstar_live", "Disney+ Hotstar live cricket match launch karta hai", "\"Hotstar live match kholo\""),
                Triple("play_podcast", "Top daily news podcast play karta hai", "\"Daily news podcast chalao\""),
                Triple("enable_bass_boost", "System audio equalizer mein bass profile select karta hai", "\"Bass boost on karo\""),
                Triple("audio_sleep_timer", "30 minute baad music auto-stop hone ka timer lagata hai", "\"30 min ka music sleep timer laga do\"")
            )),
            Triple(4, "Device Hardware & Toggles", listOf(
                Triple("toggle_flashlight", "Camera torch flashlight ko ON/OFF karta hai", "\"Torch on karo\""),
                Triple("get_battery_status", "Battery level, charging speed aur health batata hai", "\"Battery kitni bachi hai\""),
                Triple("toggle_wifi", "Wi-Fi radio toggle karta hai", "\"Wi-Fi chalu karo\""),
                Triple("toggle_bluetooth", "Bluetooth controller toggle karta hai", "\"Bluetooth band karo\""),
                Triple("toggle_hotspot", "Portable Wi-Fi hotspot switch karta hai", "\"Hotspot on kar do\""),
                Triple("toggle_dnd", "Do Not Disturb mode activate/deactivate karta hai", "\"DND mode on karo\""),
                Triple("set_brightness", "Screen display brightness level change karta hai", "\"Brightness full kar do\""),
                Triple("toggle_auto_rotate", "Screen auto-orientation lock/unlock karta hai", "\"Auto rotate on karo\""),
                Triple("toggle_airplane_mode", "Airplane mode settings prompt kholta hai", "\"Flight mode settings kholo\""),
                Triple("toggle_nfc", "NFC payment sensor settings toggle karta hai", "\"NFC on karo\""),
                Triple("lock_screen_now", "Screen ko turant lock karta hai (Emergency lockdown)", "\"Screen lock kar do\""),
                Triple("boost_ram_memory", "Background caches clean karke RAM optimize karta hai", "\"Phone ki RAM boost karo\""),
                Triple("screen_timeout_setting", "Display timeout duration customize karta hai", "\"Screen timeout 2 minute kar do\""),
                Triple("toggle_sound_profile", "Ring, Vibrate aur Silent profiles mein switch karta hai", "\"Phone vibrate par daal do\""),
                Triple("take_screenshot", "Instant screen capture leta hai", "\"Screenshot lo\"")
            ))
        )

        var idCounter = 1
        for (sec in coreSections) {
            val secNum = sec.first
            val secTitle = sec.second
            for (tool in sec.third) {
                tools.add(
                    ToolGuideItem(
                        id = "tool_${idCounter++}",
                        sectionNumber = secNum,
                        sectionTitle = secTitle,
                        toolName = tool.first,
                        kyaKartaHai = tool.second,
                        bolneWalaCommand = tool.third,
                        actionType = when (secNum) {
                            1 -> "WHATSAPP"
                            2 -> "CALL"
                            3 -> "MEDIA"
                            4 -> "DEVICE"
                            else -> "SYSTEM"
                        }
                    )
                )
            }
        }

        // 2. Programmatic Extended Suite: Categories 5 through 16 to achieve 520+ tools
        val extendedCategories = listOf(
            4 to Pair("Device & Hardware Automation", listOf(
                "gpu_turbo_mode" to "Game ke dauran frame rate boost karta hai",
                "haptic_intensity_set" to "Vibration touch feedback intensity adjust karta hai",
                "color_inversion_toggle" to "Accessibility color inversion switch karta hai",
                "dark_mode_force" to "System-wide AMOLED Dark Theme force karta hai",
                "eye_comfort_shield" to "Blue-light filter warm temperature activate karta hai",
                "cpu_frequency_monitor" to "Processor clock speed aur temperature monitor karta hai",
                "thermal_status_check" to "Device heat dissipation check karta hai",
                "speaker_water_eject" to "High-pitch sound chala kar speaker se paani nikaalta hai",
                "mic_cleaning_tone" to "Dust cleaning sound impulse run karta hai",
                "headphone_safe_volume" to "Hearing health protection volume lock karta hai",
                "ambient_display_toggle" to "Always-On Display mode switch karta hai",
                "adaptive_refresh_rate" to "Display refresh rate 120Hz/60Hz toggle karta hai",
                "power_saving_extreme" to "Battery ultra power saving protocol lagata hai",
                "storage_analyzer_pro" to "Badi files aur unused apps detect karta hai",
                "clean_junk_cache" to "Temporary thumbnail aur log files remove karta hai",
                "restart_system_ui" to "System UI glitched home screen refresh karta hai",
                "usb_debugging_toggle" to "Developer USB debugging settings prompt kholta hai",
                "wireless_charging_status" to "Qi wireless pad charging alignment check karta hai",
                "dual_app_cloner" to "Social messaging apps clone karne ki settings kholta hai",
                "one_handed_mode" to "Single hand screen shrink mode launch karta hai",
                "font_size_scale" to "Display text scale typography size adjust karta hai",
                "audio_balance_stereo" to "Left/Right earphone balance adjust karta hai",
                "mono_audio_switch" to "Single speaker mono audio merge toggle karta hai",
                "network_speed_indicator" to "Status bar internet speed live meter enable karta hai",
                "sim_data_usage_limit" to "Daily mobile data limit alert lagata hai",
                "roaming_data_toggle" to "National aur international roaming switch karta hai",
                "dns_private_set" to "Secure Ad-blocking Private DNS configure karta hai",
                "mac_randomization" to "Wi-Fi privacy MAC address shield lagata hai",
                "bluetooth_device_pair" to "Nearby Bluetooth devices scan aur connect karta hai",
                "cast_screen_tv" to "Smart TV ya Chromecast par screen mirror karta hai"
            )),
            5 to Pair("Files, Storage & Cloud", listOf(
                "list_recent_downloads" to "Download folder ki sabse nayi files dikhata hai",
                "pdf_viewer_open" to "Documents aur invoice PDF open karta hai",
                "extract_zip_archive" to "Zip aur RAR files ko extract karta hai",
                "create_zip_file" to "Selected files ko password protected ZIP banata hai",
                "duplicate_file_scanner" to "Duplicate photos aur videos scan karta hai",
                "hidden_vault_open" to "Private files secure folder lock mein store karta hai",
                "exif_metadata_strip" to "Photos se GPS location data delete karta hai",
                "compress_photo_size" to "Photo MB size ko reduce karta hai for upload",
                "convert_image_png_jpg" to "Image format JPG se PNG ya WebP convert karta hai",
                "cloud_google_drive_sync" to "Folder ko Google Drive cloud par sync karta hai",
                "cloud_dropbox_upload" to "Important doc Dropbox folder par bhejo",
                "onedrive_backup_start" to "Photos ko OneDrive cloud pe backup karta hai",
                "apk_backup_extract" to "Installed apps ka APK file backup create karta hai",
                "usb_otg_explorer" to "Connected Pen Drive / Hard Drive browse karta hai",
                "secure_shred_file" to "Files ko permanent unrecoverable overwrite karke delete karta hai",
                "audio_extractor_video" to "Video file se MP3 audio extract karta hai",
                "video_trim_clip" to "Video ke unwanted parts cut karke save karta hai",
                "scan_doc_to_pdf" to "Camera se physical pages scan karke single PDF banata hai",
                "batch_rename_files" to "Multiple files ko sequence numbers ke sath rename karta hai",
                "text_file_editor" to "Quick notes TXT files create aur edit karta hai",
                "csv_data_viewer" to "Excel spreadsheets aur CSV files preview karta hai",
                "markdown_notes_render" to "Markdown formatted notes render karke dikhata hai",
                "font_ttf_installer" to "Custom typography fonts verify karta hai",
                "subtitle_srt_fetcher" to "Movies aur videos ke Hindi/English subtitles dhundta hai",
                "torrent_downloader_view" to "Torrents aur magnet links download status monitor karta hai",
                "ftp_server_wireless" to "Phone ko Wi-Fi FTP file sharing server banata hai",
                "nfc_file_beam" to "Phone-to-phone touch file transfer trigger karta hai",
                "sd_card_benchmark" to "MicroSD card read/write speed measure karta hai",
                "recycle_bin_restore" to "Recently deleted photos aur files wapas lata hai",
                "storage_health_smart" to "Internal UFS memory health estimate karta hai"
            )),
            6 to Pair("Smart AI, Reasoning & Tools", listOf(
                "ai_deep_research" to "Web par 15+ sources scan karke comprehensive research summary deta hai",
                "ai_essay_writer" to "Topic par high quality detailed essay aur thesis draft karta hai",
                "ai_grammar_polisher" to "English aur Hindi drafts ki spelling aur grammar theek karta hai",
                "ai_code_debugger" to "Kotlin, Python, Java code bugs analyze aur fix karta hai",
                "ai_language_translate" to "100+ bhashaon mein real-time natural language translate karta hai",
                "ai_math_step_solver" to "Complex algebra aur calculus math equations step-by-step solve karta hai",
                "ai_resume_builder" to "Job profile ke hisaab se professional CV draft karta hai",
                "ai_email_generator" to "Polite, formal, ya persuasive professional email likhta hai",
                "ai_quiz_generator" to "Kisi bhi subject par interactive multiple-choice quiz banata hai",
                "ai_contract_summarizer" to "Legal terms aur policies ka saral bhasha mein summary deta hai",
                "ai_story_writer" to "Creative fantasy, sci-fi ya romantic kahaniya likhta hai",
                "ai_poem_composer" to "Chhand, shayari aur kavita compose karta hai",
                "ai_diet_meal_planner" to "Calorie goal ke hisaab se weekly Indian diet plan banata hai",
                "ai_workout_coach" to "Gym aur home workout routines design karta hai",
                "ai_trip_itinerary" to "Budget aur dates ke according day-wise travel plan deta hai",
                "ai_product_comparison" to "Do smartphones ya laptops ke specs aur value compare karta hai",
                "ai_interview_prep" to "Mock interview questions poochhta hai aur feedback deta hai",
                "ai_social_post_captions" to "Instagram reels ke liye viral captions aur hashtags likhta hai",
                "ai_youtube_script" to "Shorts aur full-length YouTube video scripts draft karta hai",
                "ai_business_name_ideas" to "Naye startup ya shop ke liye catchy brand names suggest karta hai",
                "ai_financial_budget" to "Monthly income aur kharche ka 50-30-20 rule budget banata hai",
                "ai_crypto_crypto_explainer" to "Blockchain concepts aur coin fundamentals samjhata hai",
                "ai_stock_market_concept" to "PE ratio, Market Cap aur dividend concepts explain karta hai",
                "ai_medical_term_simplifier" to "Doctor ki lab reports ke medical terms aam bhasha mein batata hai",
                "ai_tarot_horoscope" to "Daily astrological aur motivational guidance deta hai",
                "ai_joke_standup" to "Clean hilarious jokes aur witty punchlines sunata hai",
                "ai_philosophy_discussion" to "Deep philosophical questions aur ethics discuss karta hai",
                "ai_memory_flashcards" to "Exam study ke liye spaced-repetition flashcards banata hai",
                "ai_news_fact_checker" to "Viral social media messages ki authenticity verify karta hai",
                "ai_movie_recommender" to "Mood ke hisaab se best movies aur OTT series recommend karta hai"
            )),
            7 to Pair("Autonomous Missions & Tasks", listOf(
                "mission_full_trip_booking" to "Flight, hotel aur travel checklist autonomously plan karta hai",
                "mission_phone_deep_clean" to "Cache scan, duplicate purge aur memory boost autonomously run karta hai",
                "mission_morning_wakeup_prep" to "Alarm, weather briefing, stock update aur coffee reminder chalaata hai",
                "mission_night_security_lock" to "Doors check reminder, DND on, alarm check aur silent mode set karta hai",
                "mission_study_focus_mode" to "Social apps block, ambient white noise on aur 45-min timer start karta hai",
                "mission_work_standup_report" to "Calendar events aur notes se daily standup bullet points assemble karta hai",
                "mission_monthly_expense_audit" to "Bank SMS aur bills parse karke total monthly kharcha nikaalta hai",
                "mission_health_water_streak" to "Din bhar water reminders aur hydration score log karta hai",
                "mission_battery_emergency_protocol" to "10% battery par power hogs freeze karke essential SMS route karta hai",
                "mission_social_detox_scheduler" to "Instagram aur YouTube par auto-time limiter automate karta hai",
                "mission_car_highway_mode" to "Google Maps, speed camera alert, driving playlist auto-launch karta hai",
                "mission_family_safety_broadcast" to "Night travel ke dauran live GPS trusted contacts ko broadcast karta hai",
                "mission_wifi_speed_optimization" to "Fastest DNS aur ping test run karke channel analyze karta hai",
                "mission_camera_cctv_baby_monitor" to "Purane phone ko audio/motion detection security camera banata hai",
                "mission_silent_meeting_guardian" to "Calendar meeting start hone par calls auto-silent karta hai",
                "mission_gym_workout_timer" to "Exercise sets aur 60-second rest intervals bol kar guide karta hai",
                "mission_grocery_pantry_checker" to "Kitchen items ki expiry aur shopping list track karta hai",
                "mission_birthday_wishes_sender" to "Scheduled time par WhatsApp birthday message auto-send karta hai",
                "mission_backup_whatsapp_media" to "Recent WhatsApp photos ko cloud storage mein copy karta hai",
                "mission_system_firmware_check" to "Latest Android security patch update check karta hai",
                "mission_lost_phone_finder" to "Doosre phone se SMS 'RING_ALOUD' aane par full volume alarm bajata hai",
                "mission_emergency_sos_dispatch" to "Power button 5 bar dabane par police aur family ko alert bhejta hai",
                "mission_spam_call_auto_hangup" to "Spam robocalls ko pehchan kar auto-disconnect karta hai",
                "mission_offline_wikipedia_sync" to "Emergency reading ke liye offline survival guides download karta hai",
                "mission_ai_companion_chat_mode" to "Voice conversation uninterrupted interactive mode mein chalaata hai",
                "mission_auto_receipt_tax_tag" to "Shopping bill photos se GST aur expense category tag karta hai",
                "mission_daily_news_digest" to "Top 5 national aur tech headlines padh kar sunata hai",
                "mission_podcast_sleep_countdown" to "User so jaane par breathing audio band karta hai",
                "mission_custom_macro_pipeline" to "User defined multi-action sequential workflow automate karta hai",
                "mission_full_system_diagnostic" to "Screen pixels, sensors, mic, aur speakers ka test run karta hai"
            )),
            8 to Pair("Notifications & Voice Announcers", listOf(
                "announce_caller_name" to "Aane wali phone call par caller ka naam bol kar sunata hai",
                "announce_whatsapp_sender" to "WhatsApp message aane par sender ka naam bolta hai",
                "announce_sms_body" to "Incoming SMS ka text message padh kar sunata hai",
                "announce_otp_code" to "Bank transaction OTP number high-clarity voice mein bolta hai",
                "mute_notifications_while_talking" to "AI voice baatein karte waqt alerts ko mute rakhta hai",
                "spoken_battery_full_alert" to "100% charging par 'Charger hatayein' alert deta hai",
                "spoken_battery_low_warning" to "15% battery par voice alert deta hai",
                "spoken_charger_connected" to "Fast charging connect hone par voice chime deta hai",
                "spoken_wifi_disconnect_alert" to "Wi-Fi chhoot kar mobile data switch hone par alert karta hai",
                "spoken_bluetooth_connected_name" to "Bluetooth earphone connect hone par device name bolta hai",
                "spoken_alarm_weather_intro" to "Alarm band hone par subah ka temperature bol kar sunata hai",
                "spoken_app_update_ready" to "Play Store app update finish hone par bolta hai",
                "spoken_download_complete" to "Badi file download khatam hone par announce karta hai",
                "spoken_calendar_event_15min" to "Upcoming meeting se 15 minute pehle voice reminder deta hai",
                "spoken_hydration_drink_water" to "Har 2 ghante mein paani peene ka reminder bolta hai",
                "spoken_posture_reminder" to "Screen dekhte waqt seedhe baithne ka alert deta hai",
                "filter_spam_notifications" to "Shopping aur gambling promotional popups auto-block karta hai",
                "vip_contacts_notification_ring" to "Silent mode mein bhi selected VIP contacts ke notification bajata hai",
                "notification_history_browser" to "Galti se swipe hui purani notifications check karta hai",
                "heads_up_popup_toggle" to "Full screen gaming ke dauran floating banners band karta hai",
                "notification_summary_hourly" to "Pichhle 1 ghante ke saare alerts ka brief overview deta hai",
                "led_flash_on_notification" to "Alerts aane par camera flashlight blink karta hai",
                "custom_ringtone_per_app" to "WhatsApp, Telegram aur Email ke alag sound assign karta hai",
                "smart_reply_quick_suggestions" to "Notifications ke neeche one-tap AI reply pills banata hai",
                "snooze_notification_1hour" to "Kisi alert ko 1 ghante baad wapas dikhane ke liye snooze karta hai",
                "clear_all_silent_notifications" to "Low-priority notifications ko batch clear karta hai",
                "read_unread_badge_counter" to "Unread email aur message count padhta hai",
                "work_profile_notification_pause" to "Office time khatam hone par work notifications freeze karta hai",
                "ambient_edge_lighting_alert" to "Screen edges par colorful glowing light animates karta hai",
                "voice_notes_auto_transcribe" to "Audio voice notes ka text transcription notification mein deta hai"
            )),
            9 to Pair("Maps, Navigation & Travel", listOf(
                "navigate_to_home" to "Saved Ghar ki location tak Google Maps turn-by-turn navigation kholta hai",
                "navigate_to_office" to "Saved Office location tak fastest route dikhata hai",
                "find_nearby_petrol_pump" to "Current location ke paas petrol pumps aur CNG stations list karta hai",
                "find_nearby_ev_chargers" to "Electric vehicle fast charging points locate karta hai",
                "find_nearby_atm" to "Paas ke working cash ATMs dhundta hai",
                "find_nearby_hospital_er" to "Najdeeki emergency hospitals aur pharmacy dhundta hai",
                "find_nearby_restaurants" to "Top rated restaurants aur food outlets suggest karta hai",
                "find_nearby_coffee_shop" to "Peaceful work cafes aur tea points list karta hai",
                "save_current_parking_spot" to "Car parking location GPS coordinate save karta hai",
                "find_saved_parking_spot" to "Parked gaadi tak paidal chalkar jaane ka rasta dikhata hai",
                "traffic_delay_estimate" to "Current route par kitna traffic aur jam hai estimate karta hai",
                "toll_tax_calculator" to "Route par padne wale tolls ka kharcha batata hai",
                "speedometer_hud_mode" to "Windshield par reflect hone wala live speedometer display karta hai",
                "speed_camera_radar_alert" to "High speed check cameras aane par voice warning deta hai",
                "digital_magnetic_compass" to "Precise 360 degree direction aur heading batata hai",
                "barometer_altitude_meter" to "Samundar tal se unchai (Elevation in meters) measure karta hai",
                "gps_coordinate_sharer" to "WhatsApp par current latitude aur longitude bhejta hai",
                "metro_station_route" to "Najdeeki metro station aur platform route batata hai",
                "bus_schedule_tracker" to "Local city bus lines ka time table check karta hai",
                "flight_status_tracker" to "Flight number daal kar live departure/arrival status batata hai",
                "train_pnr_status_check" to "Indian Railway PNR status aur coach position check karta hai",
                "weather_current_temp" to "Current city ka temperature, humidity aur rain chance batata hai",
                "weather_7day_forecast" to "Aane wale hafte ka mausam forecast summary deta hai",
                "air_quality_index_aqi" to "Area ka PM2.5 aur AQI pollution level batata hai",
                "sunrise_sunset_times" to "Aaj ka suryoday aur suryast time batata hai",
                "offline_map_downloader" to "No-internet travel ke liye map area download karta hai",
                "hotel_booking_search" to "Budget hotels aur homestays options browse karta hai",
                "street_view_panoramas" to "Location ka 360 degree 3D photo street view kholta hai",
                "walking_calorie_route" to "Paidal chalte waqt jalne wali calories count karta hai",
                "emergency_breakdown_mechanic" to "Car puncture aur breakdown towing services locate karta hai"
            )),
            10 to Pair("Utilities & Daily Productivity", listOf(
                "scientific_calculator" to "Advanced trigonometric aur logarithmic calculation solve karta hai",
                "currency_converter_live" to "USD, EUR, GBP se INR live exchange rates convert karta hai",
                "unit_converter_distance" to "Miles se KM, Feet se Meter aur Inches convert karta hai",
                "unit_converter_weight" to "Pounds (lbs) se KG aur Grams convert karta hai",
                "unit_converter_temp" to "Celsius aur Fahrenheit convert karta hai",
                "world_clock_times" to "New York, London, Tokyo aur Dubai ka current time batata hai",
                "multi_lap_stopwatch" to "Microsecond accurate sports lap stopwatch run karta hai",
                "pomodoro_focus_timer" to "25 min focus + 5 min break cycle timer chalaata hai",
                "daily_habit_tracker" to "Reading, exercise aur habits ka streak status mark karta hai",
                "emi_loan_calculator" to "Car/Home loan ka monthly EMI aur total interest batata hai",
                "gst_tax_calculator" to "Product price par 18% ya 28% GST calculate karta hai",
                "age_birthday_calculator" to "Date of birth se exact years, months aur days nikaalta hai",
                "discount_percentage_calc" to "Sale price par % discount aur savings calculate karta hai",
                "tip_split_bill_calc" to "Doston ke sath cafe bill aur tip barabar divide karta hai",
                "notes_quick_jot" to "Voice memo ya thought ko turant bullet note mein save karta hai",
                "todo_checklist_add" to "Daily task list mein naya kaam add karta hai",
                "calendar_agenda_today" to "Aaj ke sabhi schedule events list karta hai",
                "remind_me_at_time" to "Specific time par voice alert ke sath reminder lagata hai",
                "qr_code_generator" to "Kisi bhi link ya text ka shareable QR code generate karta hai",
                "barcode_price_scanner" to "Product barcode scan karke details fetch karta hai",
                "ruler_on_screen" to "Screen par physical centimeter scale display karta hai",
                "sound_level_decibel" to "Microphone se room noise level dB measure karta hai",
                "metal_detector_sensor" to "Magnetic sensor se metal objects detect karta hai",
                "spirit_level_surface" to "Table aur surface ka level balance check karta hai",
                "morse_code_flasher" to "Torch flash se SOS morse code transmit karta hai",
                "random_coin_flipper" to "Faisle ke liye digital coin toss (Heads/Tails) karta hai",
                "random_dice_roller" to "Board game ke liye 1 se 6 number dice roll karta hai",
                "random_number_picker" to "Given range ke beech random lottery number select karta hai",
                "password_generator_strong" to "16 character ka uncrackable random password generate karta hai",
                "lorem_ipsum_generator" to "Design mockups ke liye dummy placeholder text banata hai"
            )),
            11 to Pair("Security, Privacy & Guardian", listOf(
                "app_lock_protection" to "Sensitive apps par biometric PIN lock lagata hai",
                "fake_call_escape" to "Awkward situation se nikalne ke liye fake incoming call trigger karta hai",
                "spy_camera_detector" to "Hotel rooms mein hidden infrared camera lenses detect karta hai",
                "anti_theft_motion_alarm" to "Phone uthane par tez siren horn bajata hai",
                "anti_pocket_pick_alarm" to "Pocket se phone khichne par loud alarm bajata hai",
                "wrong_pin_intruder_selfie" to "Galat password daalne wale ki secret front photo leta hai",
                "incognito_browser_launch" to "Bina search history save kiye private browser kholta hai",
                "clear_clipboard_history" to "Sensitive copied passwords ko memory se wipe karta hai",
                "camera_mic_access_indicator" to "App ke background camera/mic use par alert dot dikhata hai",
                "privacy_permissions_audit" to "Kaun si apps location aur contacts use kar rahi hain report deta hai",
                "sim_card_pin_lock" to "Phone chori hone par SIM card ko block karne ka PIN set karta hai",
                "wifi_security_risk_scan" to "Public Wi-Fi par Man-in-the-Middle attack test karta hai",
                "phishing_link_verifier" to "Suspicious link ko scan karke safe/unsafe batata hai",
                "caller_spam_reputation" to "Incoming unknown number ka trust score check karta hai",
                "secure_file_locker" to "Aadhaar, PAN aur documents ko AES-256 encrypted store karta hai",
                "emergency_quick_dial_112" to "National Emergency Hotline 112 par direct route karta hai",
                "women_safety_sos_beacon" to "Nearest police station ko audio recording ke sath alert bhejta hai",
                "parental_screen_time_limit" to "Bachhon ke liye daily gaming duration limit lagata hai",
                "restricted_guest_mode" to "Phone kisi ko dene par sirf dialer allow karta hai",
                "usb_data_blocker" to "Charging stations par data transfer block karke sirf charge hone deta hai",
                "device_admin_lockout" to "Remote command aane par device freeze kar deta hai",
                "bluetooth_airdrop_block" to "Unknown devices se unsolicited file beams reject karta hai",
                "screen_pinning_lock" to "Current app ko screen par freeze karke back navigation block karta hai",
                "sim_removal_siren" to "SIM card nikalne par loudly sound karta hai",
                "encrypt_sd_card_storage" to "Memory card ki files ko encrypt karta hai",
                "audit_installed_apks" to "Play Protect bypass karne wali sideloaded apps flag karta hai",
                "secure_dns_over_https" to "ISP browsing snooping se bachne ke liye DoH enable karta hai",
                "kill_background_trackers" to "Analytics aur telemetry trackers ko block karta hai",
                "privacy_screen_shade" to "Side angles se dekhne walo se bachne ke liye screen dim karta hai",
                "factory_reset_protection" to "Google account password ke bina device wipe block karta hai"
            )),
            12 to Pair("Health, Fitness & Wellness", listOf(
                "step_counter_pedometer" to "Aaj chale gaye total steps aur walking distance track karta hai",
                "water_intake_logger" to "Gilas count karke 3 liter daily water goal track karta hai",
                "calorie_counter_meal" to "Khaye gaye khane ki estimated calories log karta hai",
                "bmi_body_mass_index" to "Height aur weight se body health index nikaalta hai",
                "heart_rate_cam_sensor" to "Camera flash sensor se pulse rate estimate karta hai",
                "breathing_exercise_relax" to "4-7-8 deep relaxation breathing animation chalaata hai",
                "guided_meditation_session" to "10 minute ka calm mindfulness session play karta hai",
                "sleep_hours_logger" to "Raat ki neend duration aur sleep quality score karta hai",
                "eye_strain_20_20_rule" to "Har 20 min mein 20 feet door dekhne ka reminder deta hai",
                "desk_stretch_reminder" to "Baithe rehne par quick neck aur shoulder stretch guide karta hai",
                "daily_step_goal_alert" to "10,000 steps complete hone par celebration chime deta hai",
                "workout_interval_timer" to "HIIT training ke liye High/Low intensity timer bajata hai",
                "cycling_tracker_gps" to "Cycle chalaane ka speed aur route distance log karta hai",
                "running_pace_meter" to "Running ke dauran minutes per kilometer pace batata hai",
                "sugar_fasting_tracker" to "Diabetes sugar levels date ke sath record karta hai",
                "blood_pressure_log" to "Systolic aur Diastolic BP readings history save karta hai",
                "menstrual_cycle_tracker" to "Next period date aur ovulation calendar estimate karta hai",
                "medicine_pill_reminder" to "Dawai lene ka time aane par voice reminder deta hai",
                "doctor_appointment_alert" to "Upcoming clinic visit ka alert schedule karta hai",
                "white_noise_rain_sounds" to "Neend ke liye baarish aur ocean waves sounds chalaata hai",
                "binaural_beats_focus" to "Brain concentration ke liye Alpha waves frequency play karta hai",
                "healthy_snack_recommender" to "Craving ke waqt low-calorie Indian snacks suggest karta hai",
                "daily_sunlight_vitamin_d" to "Subah ki dhoop lene ka 15-minute alert deta hai",
                "posture_check_camera" to "Screen distance analyze karke posture check karta hai",
                "hearing_test_frequency" to "High pitch frequencies sun kar hearing capability test karta hai",
                "first_aid_cpr_instructions" to "Emergency CPR karne ke bol kar steps guide karta hai",
                "burn_injury_first_aid" to "Jalne par immediate cooling steps batata hai",
                "snake_bite_emergency_guide" to "Poisonous bite ke do's aur don'ts batata hai",
                "panic_attack_grounding" to "5-4-3-2-1 sensory grounding exercise voice se guide karta hai",
                "fitness_streak_badge" to " लगातार workout days ka achievement badge dikhata hai"
            )),
            13 to Pair("Smart Home & IoT Automation", listOf(
                "smart_bulb_toggle_all" to "Ghar ki sabhi smart LED lights ON/OFF karta hai",
                "smart_bulb_color_warm" to "Living room lights ko relaxing warm yellow set karta hai",
                "smart_bulb_brightness_50" to "Bedroom lights ko 50% dim karta hai",
                "smart_ac_temperature_set" to "Air Conditioner temperature 24 degree set karta hai",
                "smart_ac_mode_cool" to "AC ko auto cooling mode par switch karta hai",
                "smart_fan_speed_control" to "Smart ceiling fan speed 3 par adjust karta hai",
                "smart_tv_power_toggle" to "Android TV ko remote ki tarah ON ya OFF karta hai",
                "smart_tv_input_hdmi" to "TV source input HDMI 1 gaming console par badalta hai",
                "smart_tv_volume_adjust" to "TV volume mute ya un-mute karta hai",
                "smart_plug_geyser_on" to "Bathroom geyser smart plug ko 20 minute ke liye on karta hai",
                "smart_plug_mosquito_repellent" to "Raat ko mosquito machine smart plug auto-on karta hai",
                "robot_vacuum_start_clean" to "Roborock vacuum cleaner ko hall clean karne bhejta hai",
                "robot_vacuum_dock_charge" to "Vacuum cleaner ko charging dock par wapas bulata hai",
                "cctv_live_doorbell_cam" to "Main gate video doorbell ka live camera view screen par laata hai",
                "smart_door_lock_status" to "Main entrance door locked hai ya open check karta hai",
                "home_away_mode_trigger" to "Ghar se nikalte hi sabhi lights aur AC band karta hai",
                "home_arrived_mode_trigger" to "Ghar aane par porch light aur hallway lights on karta hai",
                "movie_night_scene" to "TV on, lights dim aur curtains close karne ka scene banata hai",
                "good_morning_home_scene" to "Geyser on, curtains open aur morning news play karta hai",
                "smart_curtains_open" to "Motorized window curtains open karta hai",
                "smart_curtains_close" to "Window curtains close karta hai",
                "smart_purifier_turbo" to "Air Purifier ko high speed air clean mode par chalata hai",
                "smart_geyser_auto_off" to "Paani garam hone par power cut off karta hai",
                "smart_camera_motion_alert" to "Backyard mein movement hone par phone par alert bhejta hai",
                "chromecast_stream_music" to "Home theater speakers par multi-room music sync karta hai",
                "alexa_device_announce" to "Ghar ke Echo smart speakers par 'Dinner ready hai' bolta hai",
                "google_nest_broadcast" to "Google Nest hubs par custom voice message announce karta hai",
                "smart_sprinkler_lawn_water" to "Garden sprinklers 10 minute ke liye start karta hai",
                "smart_kitchen_chimney_on" to "Cooking ke dauran exhaust chimney power on karta hai",
                "home_energy_meter_watts" to "Ghar ka real-time electricity load consumption batata hai"
            )),
            14 to Pair("Developers, Cloud & Terminal", listOf(
                "network_ping_test" to "Google server ko ping karke latency ms test karta hai",
                "traceroute_ip_hop" to "Network packets ke routing hops trace karta hai",
                "dns_lookup_nslookup" to "Domain name ke A, MX aur TXT DNS records fetch karta hai",
                "whois_domain_checker" to "Domain registrar aur expiry date details nikaalta hai",
                "public_ip_detector" to "Aapka external WAN public IP address batata hai",
                "internet_speed_fast_test" to "Real-time download aur upload bandwidth speed measure karta hai",
                "base64_string_encode" to "Raw text ko Base64 format mein encode karta hai",
                "base64_string_decode" to "Base64 encoded string ko readable text banata hai",
                "url_encoder_decoder" to "Web URLs ke special characters encode/decode karta hai",
                "md5_sha256_hash_calc" to "Text aur files ka cryptographic checksum calculate karta hai",
                "json_syntax_formatter" to "Minified JSON ko clean indented format mein view karta hai",
                "jwt_token_payload_inspect" to "JSON Web Token (JWT) expire date aur claims check karta hai",
                "regex_pattern_evaluator" to "Regular expression pattern matching test karta hai",
                "unix_epoch_converter" to "Timestamp seconds ko human readable date mein badalta hai",
                "color_hex_rgb_convert" to "Hex codes (#FF5733) ko RGB aur HSL values mein convert karta hai",
                "html_entity_escaper" to "HTML code tags ko escape karke web-safe text banata hai",
                "markdown_to_html_compile" to "Markdown notes ko web page HTML structure mein render karta hai",
                "sql_query_formatter" to "Complex database SELECT queries beautify karta hai",
                "curl_api_post_tester" to "REST API endpoint par quick GET/POST test request bhejta hai",
                "git_commit_status_view" to "GitHub repository commits aur pull requests check karta hai",
                "docker_container_monitor" to "Connected server par running docker containers list karta hai",
                "ssh_terminal_quick_run" to "VPS Linux server se secure shell session open karta hai",
                "port_checker_open" to "Specific IP address par port 80/443/22 open hai ya closed batata hai",
                "http_status_codes_reference" to "404, 500, 401, 302 HTTP status codes ka meaning batata hai",
                "crontab_schedule_syntax" to "Cron job timing expression ka human explanation deta hai",
                "ascii_table_character_map" to "ASCII character code values list karta hai",
                "uuid_v4_batch_generator" to "Database ke liye unique UUID strings generate karta hai",
                "git_ignore_template_fetch" to "Android, Node.js ya Python ke liye .gitignore banata hai",
                "svg_vector_code_preview" to "SVG vector XML code ko canvas par live render karta hai",
                "server_uptime_health_ping" to "Personal website online hai ya down alert monitor karta hai"
            )),
            15 to Pair("AI Company Voice Profiles & Engines", listOf(
                "voice_openai_alloy" to "OpenAI Alloy balanced neutral voice activate karta hai",
                "voice_openai_echo" to "OpenAI Echo deep male studio voice activate karta hai",
                "voice_openai_fable" to "OpenAI Fable expressive storytelling voice activate karta hai",
                "voice_openai_onyx" to "OpenAI Onyx authoritative rich baritone voice activate karta hai",
                "voice_openai_nova" to "OpenAI Nova warm energetic female studio voice activate karta hai",
                "voice_openai_shimmer" to "OpenAI Shimmer clear high-treble studio voice activate karta hai",
                "voice_gemini_puck" to "Google Gemini Puck playful conversational voice lagata hai",
                "voice_gemini_charon" to "Google Gemini Charon mature intellectual voice lagata hai",
                "voice_gemini_kore" to "Google Gemini Kore soothing helpful companion voice lagata hai",
                "voice_gemini_fenrir" to "Google Gemini Fenrir crisp professional assistant voice lagata hai",
                "voice_gemini_aoede" to "Google Gemini Aoede musical lyrical female voice lagata hai",
                "voice_groq_ultra_speed" to "Groq LPU ultra-low latency lightning-fast voice activate karta hai",
                "voice_deepseek_reasoning" to "DeepSeek thoughtful analytical response voice activate karta hai",
                "voice_openrouter_multi" to "OpenRouter multi-provider neural fallback voice lagata hai",
                "persona_jarvis_tony_stark" to "Tony Stark ka Jarvis British Butler AI persona activate karta hai",
                "persona_friday_assistant" to "Irish accented friendly female tactical persona activate karta hai",
                "persona_anime_companion" to "Cute, enthusiastic energetic anime voice personality lagata hai",
                "persona_guru_scholar" to "Wise ancient Indian philosopher calm guru persona lagata hai",
                "persona_hindi_desi_dost" to "Desi humor aur friendly Hindi/Hinglish dost style lagata hai",
                "persona_motivational_coach" to "High-energy gym trainer motivational speaker style lagata hai",
                "persona_corporate_executive" to "Strict professional polished boardroom style lagata hai",
                "persona_kindergarten_teacher" to "Bacho ke liye saral aur pyari bhasha mein samjhane wala persona",
                "persona_cyberpunk_neon" to "Futuristic dystopian sci-fi cybernetic voice effect",
                "persona_radio_jockey" to "Upbeat RJ voice with dynamic radio modulation",
                "persona_calm_zen_monk" to "Slow, soothing whisper peaceful meditation guide persona"
            ))
        )

        for (ext in extendedCategories) {
            val secNum = ext.first
            val secTitle = ext.second.first
            val itemList = ext.second.second
            for (tool in itemList) {
                tools.add(
                    ToolGuideItem(
                        id = "tool_${idCounter++}",
                        sectionNumber = secNum,
                        sectionTitle = secTitle,
                        toolName = tool.first,
                        kyaKartaHai = tool.second,
                        bolneWalaCommand = "\"MJ, ${tool.first.replace('_', ' ')}\"",
                        actionType = when (secNum) {
                            4 -> "DEVICE"
                            5 -> "FILES"
                            6 -> "RESEARCH"
                            7 -> "MISSION"
                            8 -> "NOTIF"
                            9 -> "MAPS"
                            10 -> "CLIPBOARD"
                            11 -> "LOCK"
                            12 -> "BATTERY"
                            13 -> "AUTOMATION"
                            14 -> "BROWSER"
                            15 -> "VOICE"
                            else -> "SYSTEM"
                        }
                    )
                )
            }
        }

        // 3. Section 16: Deep App Voice Connectors & Launchers (108 items) -> Total 530+ Tools
        val appList = listOf(
            "WhatsApp", "Instagram", "YouTube", "Spotify", "Snapchat", "Telegram", "Twitter / X",
            "Facebook", "LinkedIn", "Reddit", "Discord", "Pinterest", "Netflix", "Amazon Prime",
            "Disney+ Hotstar", "JioCinema", "SonyLIV", "Zee5", "VLC Media Player", "SoundCloud",
            "Google Chrome", "Mozilla Firefox", "Opera", "DuckDuckGo", "Gmail", "Google Drive",
            "Google Photos", "Google Maps", "Google Pay", "PhonePe", "Paytm", "CRED", "Amazon",
            "Flipkart", "Myntra", "Meesho", "Ajio", "Zomato", "Swiggy", "Blinkit", "Zepto",
            "Uber", "Ola Cabs", "InDrive", "MakeMyTrip", "IRCTC Rail", "BookMyShow", "Canva",
            "Duolingo", "Truecaller", "Shazam", "Snapseed", "Lightroom", "CapCut", "PicsArt",
            "InShot", "KineMaster", "Notion", "Trello", "Slack", "Microsoft Teams", "Zoom",
            "Google Meet", "Google Keep", "Evernote", "Bitwarden", "1Password", "GitHub",
            "Stack Overflow", "Medium", "Quora", "Wikipedia", "Google Classroom", "Khan Academy",
            "Coursera", "Udemy", "LinkedIn Learning", "TradingView", "Zerodha Kite", "Groww",
            "Upstox", "Angel One", "Binance", "CoinDCX", "WazirX", "Moneycontrol", "Mint",
            "Dailyhunt", "Inshorts", "Times of India", "NDTV", "BBC News", "Cricbuzz", "ESPNCricinfo",
            "Strava", "Fitbit", "Nike Training Club", "Google Fit", "Calm", "Headspace", "Healthifyme",
            "Tata Neu", "JioMart", "BigBasket", "Lenskart", "Nykaa", "Urban Company", "Dunzo"
        )
        for (appName in appList) {
            val key = appName.lowercase()
                .replace(" ", "_")
                .replace("+", "plus")
                .replace("/", "_")
                .replace("-", "_")
            tools.add(
                ToolGuideItem(
                    id = "tool_${idCounter++}",
                    sectionNumber = 16,
                    sectionTitle = "App Launchers & Deep Link Connectors",
                    toolName = "launch_${key}",
                    kyaKartaHai = "$appName app ko voice se turant launch karta hai aur search open karta hai",
                    bolneWalaCommand = "\"Open $appName\", \"$appName kholo\"",
                    actionType = "APP"
                )
            )
        }

        return tools
    }
}
