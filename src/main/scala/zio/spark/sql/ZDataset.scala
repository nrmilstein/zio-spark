package zio.spark.sql
import org.apache.spark.sql.{DataFrame => UnderlyingDataFrame}

import zio.Task

final case class ZDataset[T](df: UnderlyingDataFrame) extends Dataset[T] {
  override def limit(n: Int): Dataset[T] = transformation(_.limit(n))

  def transformation(f: UnderlyingDataFrame => UnderlyingDataFrame): Dataset[T] = ZDataset(f(df))

  override def count(): Task[Long] = action(_.count())

  def action[A](f: UnderlyingDataFrame => A): Task[A] = Task.attemptBlocking(f(df))
}
