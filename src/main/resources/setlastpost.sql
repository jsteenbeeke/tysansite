UPDATE forumthread ft 
SET lastpost = subquery.lp
FROM (SELECT MAX(time) AS lp, thread_id FROM forumpost GROUP BY thread_id) AS subquery
WHERE subquery.thread_id = ft.id;
