
insert into `certificate` (`certificate_id`, `name`)
values (1, '정보처리기사');


insert into `member` (`member_id`, `email`, `nickname`, `profile_image`, `oauth_id`, `oauth_server`, `deleted`,
                      `current_certificate_id`, `last_access_at`, `created_at`, `modified_at`)
values (1, 'email@email.com', 'hyeok', 'profile', '1', 'KAKAO', 0, 1, null, null, null),
       (2, 'email2@email.com', 'hyeok2', 'profile', '2', 'KAKAO', 0, 1, null, null, null);


insert into `device_token`(`device_token_id`, `device_id`,`token`,`member_id`,`created_at`,`updated_at`)
values (1, '1', 'dsvy0OALlGGBfG-CJenhet:APA91bF-8RCvYbXojGjV2BkvYccTDiXhbHtwtyCM-nMHCOvCH6vLgPbTxig46mVB06GaA5NdAmxoqjZcay6AWViTsOoRwL4ovtOo4eIcJHY1PpnCpkttDHY', 1, '2025-02-04',null),
       (2, '2', 'dsvy0OALlGGBfG-CJenhet:APA91bF-8RCvYbXojGjV2BkvYccTDiXhbHtwtyCM-nMHCOvCH6vLgPbTxig46mVB06GaA5NdAmxoqjZcay6AWViTsOoRwL4ovtOo4eIcJHY1PpnCpkttDHY', 2, '2025-02-04',null);



insert into `plan` (`plan_id`, `end_at`,`created_at`, `certificate_id`, `member_id`)
values (1, '2025-12-03','2024-12-01', 1, 1),
     ( 2, '2025-12-03','2024-12-01', 1, 2);

insert into `plan_item` (`plan_item_id`, `plan_id`, `activity_type`, `goal_type`, `target_value`, `created_at`)
values
    (1, 1, 'EXAM', 'DAILY', 100, '2024-12-01'),
    (2, 1, 'STUDY', 'DAILY', 100, '2024-12-01'),
    (3, 1, 'TIME', 'DAILY', 100, '2024-12-01'),
    (4, 2, 'EXAM', 'DAILY', 100, '2024-12-01'),
    (5, 2, 'STUDY', 'DAILY', 100, '2024-12-01'),
    (6, 2, 'TIME', 'DAILY', 100, '2024-12-01');

insert into `plan_progress` (`plan_progress_id`, `plan_id`,`target_value`,`completed_value`, `activity_type`,`goal_type`,`progress_date`)
values (1,1,100,100,'EXAM', 'DAILY', DATEADD('DAY',-1, CURRENT_DATE)),
       (2,1,100,100,'STUDY', 'DAILY', DATEADD('DAY',-1, CURRENT_DATE)),
       (3,1,100,100,'TIME', 'DAILY', DATEADD('DAY',-1, CURRENT_DATE)),
       (4,2,100,100,'EXAM', 'DAILY', DATEADD('DAY',-1, CURRENT_DATE)),
       (5,2,100,100,'STUDY', 'DAILY', DATEADD('DAY',-1, CURRENT_DATE)),
       (6,2,100,100,'TIME', 'DAILY', DATEADD('DAY',-1, CURRENT_DATE));
