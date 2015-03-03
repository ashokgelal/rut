package io.danielnorberg.rut;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RouterTest {

  @Test
  public void testRouting() {
    final Router.Builder<String> builder = Router.builder();
    final String target = "target";
    builder.route("GET", "/foo/<bar>/baz", target);

    final Router<String> router = builder.build();

    final Router.Result<String> result = router.result();

    final Router.Status status1 = router.route("GET", "/foo/bar-value/baz", result);
    assertThat(status1, is(Router.Status.SUCCESS));
    assertThat(result.status(), is(Router.Status.SUCCESS));
    assertThat(result.isSuccess(), is(true));
    assertThat(result.target(), is(target));
    final String name = result.paramName(0);
    final CharSequence value = result.paramValue(0);
    assertThat(name, is("bar"));
    assertThat(value.toString(), is("bar-value"));

    final Router.Status status2 = router.route("DELETE", "/foo/bar/baz", result);
    assertThat(status2, is(Router.Status.METHOD_NOT_ALLOWED));
    assertThat(result.status(), is(Router.Status.METHOD_NOT_ALLOWED));
    assertThat(result.isSuccess(), is(false));

    final Router.Status status3 = router.route("GET", "/non/existent/path", result);
    assertThat(status3, is(Router.Status.NOT_FOUND));
    assertThat(result.status(), is(Router.Status.NOT_FOUND));
    assertThat(result.isSuccess(), is(false));
  }


}