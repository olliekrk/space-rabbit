package util

import com.github.javafaker.Faker

object FakeUtils {

  private val faker: Faker = Faker.instance()

  def fakeCompany: String = faker.company().name()

  def fakeDescription: String = faker.chuckNorris().fact()

}
