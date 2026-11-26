// lib/services/cache_local.dart
import 'package:drift/drift.dart';
import 'package:drift_flutter/drift_flutter.dart';

part 'cache_local.g.dart';   // generado con: dart run build_runner build

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
	CacheDb.abrir() : super(driftDatabase(name: 'agrocadena_cache'));
	@override int get schemaVersion => 1;
	
	Future<void> guardar(String c, DateTime t, double temp, double hr) =>
	into(lecturasCache).insert(LecturasCacheCompanion.insert(
	contenedor: c, instante: t, temperatura: temp, humedad: hr));
	
	Future<List<LecturasCacheData>> ultimas(String c) =>
	(select(lecturasCache)..where((l) => l.contenedor.equals(c))
	..orderBy([(l) => OrderingTerm.desc(l.instante)])
	..limit(50)).get();
}
