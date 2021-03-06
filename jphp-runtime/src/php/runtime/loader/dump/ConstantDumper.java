package php.runtime.loader.dump;

import php.runtime.Memory;
import php.runtime.common.Modifier;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.loader.dump.io.DumpInputStream;
import php.runtime.loader.dump.io.DumpOutputStream;
import php.runtime.reflection.ConstantEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ConstantDumper extends Dumper<ConstantEntity> {
    public ConstantDumper(Context context, Environment env, boolean debugInformation) {
        super(context, env, debugInformation);
    }

    @Override
    public int getType() {
        return Types.CONSTANT;
    }

    @Override
    public void save(ConstantEntity entity, OutputStream output) throws IOException {
        DumpOutputStream dump = new DumpOutputStream(output);

        dump.writeBoolean(entity.isCaseSensitise());
        dump.writeEnum(Modifier.PUBLIC);
        dump.writeName(entity.getName());
        dump.writeTrace(debugInformation ? null : entity.getTrace());
        dump.writeMemory(entity.getValue());

        dump.writeRawData(null);
    }

    @Override
    public ConstantEntity load(InputStream input) throws IOException {
        DumpInputStream dump = new DumpInputStream(input);

        boolean cs = dump.readBoolean();
        Modifier modifier = dump.readModifier();
        String name = dump.readName();
        TraceInfo trace = dump.readTrace(context);
        Memory value = dump.readMemory();

        ConstantEntity entity = new ConstantEntity(name, value, cs);
        entity.setContext(context);
        entity.setTrace(trace);

        dump.readRawData();
        return entity;
    }
}
