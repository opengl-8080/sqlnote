#REST Spec
##リクエスト一覧
- 全 SQL メモのタイトルと ID を問い合わせる
- SQL メモを新規に登録する
- SQL メモを削除する
- ID を指定して SQL メモの情報を取得する
- SQL メモの情報を更新する
- SQL を実行し、結果を取得する

-----------------------------------------------------------------------------------------------
##全 SQL メモのタイトルと ID を問い合わせる
###Method
`GET`

###Path
`/sql`

###Response
```json
[
	{
		"id": 1,
		"title": "～～を検索する",
		"url": "http://localhots:1234/sqlnote/sql/1"
	},
	{
		"id": 2,
		"title": "～～を検索する",
		"url": "http://localhots:1234/sqlnote/sql/2"
	}
]
```

-----------------------------------------------------------------------------------------------
##SQL メモを新規に登録する
###Method
`POST`

###Path
`/sql`


-----------------------------------------------------------------------------------------------
##SQL メモを削除する
###Method
`DELETE`

###Path
`/sql/{id}`

-----------------------------------------------------------------------------------------------
##ID を指定して SQL メモの情報を取得する
###Method
`GET`

###Path
`/sql/{id}`

###Response
```json
{
	"id": 5,
	"title": "～～を検索する",
	"sql": "SELECT *\n FROM TEST_TABLE",
	"parameterNames": ["CODE", "NAME"],
	"executeSqlUrl": "http://localhost:1234/sqlnote/sql/5/result"
}
```

-----------------------------------------------------------------------------------------------
##SQL メモの情報を更新する
###Method
`PUT`

###Path
`/sql/{id}`

###Request Body
```json
{
	"title": "～～を検索する",
	"sql": "SELECT *\n FROM TEST_TABLE",
	"parameterNames": ["CODE", "NAME"]
}
```

-----------------------------------------------------------------------------------------------
##SQL を実行し、結果を取得する
###Method
`GET`

###Path
`/sql/{id}/result`

###Request Body
```json
{
	"CODE": "AAA",
	"NAME": "BBB"
}
```

- Search Parameters.

###Response Body
####200 OK
```json
[
	{
		"CODE": "AAA",
		"NAME": "BBB",
		"CREATE_DATE": "2012-01-01T12:00+09:00"
	},
	...
]
```

####303 See Other
```json
{
	"recordCount": 1500,
	"url": "http://localhost:1234/sqlnote/sql/5/result?type=csv"
}
```

- 検索結果が 1,000 より大きい場合、 CSV 出力を促す。

