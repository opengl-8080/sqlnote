#REST Spec
##リクエスト一覧
- 全 SQL メモのタイトルと ID を問い合わせる
- ID を指定して SQL メモの情報を取得する
- SQL メモの情報を更新する
- SQL メモを新規に登録する
- SQL メモの格納場所を変更する
- SQL メモを削除する
- SQL を実行し、結果を取得する
- カテゴリを追加する
- カテゴリを移動する
- カテゴリを削除する
- カテゴリの名前を変更する

##全 SQL メモのタイトルと ID を問い合わせる
###Method
`GET`

###Path
`/sql`

###Response
```json
{
	"id": 1,
	"title": "/",
	"children": [
		{
			"id": 2,
			"title": "～～関連",
			"children": [
				{
					"id": 5,
					"title": "～～を検索する",
					"url": "http://localhots:1234/sqlnote/sql/5"
				}
			]
		},
		{
			"id": 3,
			"title": "～～と～～を検索する",
			"url": "http://localhost:1234/sqlinote/sql/3"
		}
	]
}
```

##SQL メモを新規に登録する
###Method
`POST`

###Path
`/sql`

###Request Body
```json
{
	"appendTo": 1
}
```

- appendTo = categoryId

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

##SQL メモを削除する
###Method
`DELETE`

###Path
`/sql/{id}`

##SQL メモの格納場所を変更する
###Method
`PUT`

###Path
`/sql/{id}?moveTo={categoryId}`

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

##カテゴリを追加する
###Method
`POST`

###Path
`/category`

###Request Body
```json
{
	"appendTo": 10,
	"title": "～～について"
}
```

- appendTo = categoryId

##カテゴリを移動する
###Method
`PUT`

###Path
`/category/{id}?moveTo={cagegoryId}`

##カテゴリを削除する
###Method
`DELETE`

###Path
`/category/{id}`

##カテゴリの名前を変更する
###Method
`PUT`

###Path
`/category/{id}`

###Request Body
```json
{
	"title": "～～関連"
}
```
