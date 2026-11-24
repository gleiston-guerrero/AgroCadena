// lib/services/cache_local.dart
import 'package:drift/drift.dart';

class LecturasCache extends Table {
	IntColumn  get id          => integer().autoIncrement()();
	TextColumn get contenedor  => text()();
	DateTimeColumn get instante=> dateTime()();
	RealColumn get temperatura => real()();
	RealColumn get humedad     => real()();
}

@DriftDatabase(tables: [LecturasCache])
class CacheDb extends _$CacheDb {
	CacheDb(QueryExecutor e) : super(e);
	@override int get schemaVersion => 1;
	
	Future<void> guardar(String c, DateTime t, double temp, double hr) =>
	into(lecturasCache).insert(LecturasCacheCompanion.insert(
	contenedor: c, instante: t, temperatura: temp, humedad: hr));
	
	Future<List<LecturasCacheData>> ultimas(String c) =>
	(select(lecturasCache)..where((l) => l.contenedor.equals(c))
	..orderBy([(l) => OrderingTerm.desc(l.instante)])
	..limit(50)).get();
}
